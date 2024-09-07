package com.example.izvorlocator.data.register

data class User(
    var firstname : String = "",
    var lastname : String = "",
    var email : String = "",
    var phone : String = "",
    var points : Int = 0 //broj poena, ovde se dodaje kad korisnik npr. kreira objavu ili nesto
)