package com.example.savethefood.local.domain

//User
data class User(var userName: String = "",
                 var userEmail: String = "",
                 var userPassword: String = "") {

    constructor() : this("", "","")
}