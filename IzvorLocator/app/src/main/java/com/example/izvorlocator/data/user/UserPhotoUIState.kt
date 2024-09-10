package com.example.izvorlocator.data.user

import android.net.Uri

data class UserPhotoUIState(
    var firstname: String = "",
    var lastname: String = "",
    var email: String = "",
    var phone: String = "",
    var points: Int = 0,
    var profilePhotoUri: Uri? = null
)