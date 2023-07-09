package com.codelog.arke.api

class User(var username: String) {
    var firstName: String = username.uppercase()
}