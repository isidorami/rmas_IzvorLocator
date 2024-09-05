package com.example.izvorlocator.data.register

data class RegisterUIState(
    var firstname : String = "",
    var lastname : String = "",
    var email : String = "",
    var phone : String = "",
    var password : String = "",

    var firstnameError : Boolean = true,
    var lastnameError : Boolean = true,
    var emailError : Boolean = true,
    var phoneError : Boolean = true,
    var passwordError : Boolean = true
)