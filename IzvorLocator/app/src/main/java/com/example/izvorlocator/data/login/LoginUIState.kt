package com.example.izvorlocator.data.login

data class LoginUIState (
    var email : String = "",
    var password : String = "",

    var emailError : Boolean = true,
    var passwordError : Boolean = true
)