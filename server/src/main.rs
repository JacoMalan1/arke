use log::warn;
use std::{env, time::SystemTime};
use tokio::{
    io::AsyncReadExt,
    net::{TcpListener, TcpStream},
};

mod user;

async fn process_socket(mut socket: TcpStream) {
    if let Err(err) = socket.readable().await {
        eprintln!("Failed to wait for socket to become readable: {err:?}");
        return;
    }

    let mut buffer = [0u8; 512];
    if let Err(err) = socket.read(&mut buffer).await {
        eprintln!("Failed to read from socket: {err:?}");
        return;
    }

    println!(
        "Got message: {}",
        String::from_utf8(Vec::from(buffer.as_ref())).unwrap()
    );
}

#[cfg(debug_assertions)]
const LOG_LEVEL: log::LevelFilter = log::LevelFilter::Debug;
#[cfg(not(debug_assertions))]
const LOG_LEVEL: log::LevelFilter = log::LevelFilter::Info;

fn setup_logger() -> Result<(), fern::InitError> {
    fern::Dispatch::new()
        .format(|out, message, record| {
            out.finish(format_args!(
                "{}[{}][{}] {}",
                humantime::format_rfc3339_seconds(SystemTime::now()),
                record.target(),
                record.level(),
                message
            ))
        })
        .level(LOG_LEVEL)
        .chain(std::io::stdout())
        .chain(fern::log_file(format!(
            "/var/log/alan/alan_log_{}.log",
            std::time::SystemTime::now()
                .duration_since(std::time::UNIX_EPOCH)
                .unwrap()
                .as_millis()
        ))?)
        .apply()?;
    Ok(())
}

#[tokio::main]
async fn main() -> Result<(), tokio::io::Error> {
    setup_logger().expect("Couldn't setup logger");

    if let Err(err) = dotenvy::dotenv() {
        warn!("Couldn't load .env file: {err:?}");
    }

    let bind_addr = env::var("BIND_ADDRESS").unwrap_or(String::from("127.0.0.1"));
    let bind_port = env::var("BIND_PORT").unwrap_or(String::from("8080"));
    let listener = TcpListener::bind(format!("{bind_addr}:{bind_port}")).await?;

    loop {
        let (socket, _) = listener.accept().await?;
        process_socket(socket).await;
    }
}
