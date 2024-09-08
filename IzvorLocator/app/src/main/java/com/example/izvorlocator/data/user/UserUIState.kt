package com.example.izvorlocator.data.user

import androidx.compose.runtime.MutableState

data class UserUIState(
    var firstname : String = "",
    var lastname : String = "",
    var email : String = "",
    var phone : String = "",
    var points : Int = 0 //broj poena, ovde se dodaje kad korisnik npr. kreira objavu ili nesto
)