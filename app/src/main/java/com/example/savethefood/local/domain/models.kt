package com.example.savethefood.local.domain

//User
data class User(var username: String = "",
                 var email: String = "",
                 var password: String = "") {

    constructor() : this("", "","")
}