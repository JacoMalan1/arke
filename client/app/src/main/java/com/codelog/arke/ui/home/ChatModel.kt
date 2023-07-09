package com.codelog.arke.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.codelog.arke.R
import com.codelog.arke.api.User
import com.google.android.material.snackbar.Snackbar
import java.security.KeyStore
import java.security.cert.CertificateFactory
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManagerFactory

data class ChatModel(val user: User)

class ChatAdapter(chatModels: ArrayList<ChatModel>): RecyclerView.Adapter<ChatAdapter.ViewHolder>() {
    private val chatModels: ArrayList<ChatModel>

    init {
        this.chatModels = chatModels
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val textView: TextView
        val clChatCard: ConstraintLayout

        init {
            textView = itemView.findViewById(R.id.tvUsername)
            clChatCard = itemView.findViewById(R.id.clChatCard)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.chat_layout, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return chatModels.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = chatModels[position]
        holder.textView.text = model.user.username
        holder.clChatCard.setOnClickListener { view ->
            // TODO: Open chat activity

            val thread = Thread {
                try {
                    val cf = CertificateFactory.getInstance("X.509")
                    val keyStream = view.context.resources.openRawResource(R.raw.cert)
                    val ca = cf.generateCertificate(keyStream)
                    keyStream.close()

                    val ks = KeyStore.getInstance(KeyStore.getDefaultType())
                    ks.load(null, null)
                    ks.setCertificateEntry("ca", ca)

                    val tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
                    tmf.init(ks)
                    val sslContext = SSLContext.getInstance("TLSv1.3")
                    sslContext.init(null, tmf.trustManagers, null)

                    val factory = sslContext.socketFactory
                    val socket = factory.createSocket("10.0.2.2", 8080)
                    socket.getOutputStream().write("Something\n".toByteArray())
                    val line = socket.getInputStream().bufferedReader().readLine();

                    Snackbar.make(view, line, Snackbar.LENGTH_LONG).show()
                    socket.close()
                } catch (e: Exception) {
                    Snackbar.make(view, "ERR: $e", Snackbar.LENGTH_LONG).show()
                }
            }
            thread.start()
        }
    }
}