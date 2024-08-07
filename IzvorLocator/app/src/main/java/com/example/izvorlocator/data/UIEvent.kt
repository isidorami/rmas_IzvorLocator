package com.example.izvorlocator.data

sealed class UIEvent {
    data class FirstnameChanged(val firstname: String): UIEvent()
    data class LastnameChanged(val lastname: String): UIEvent()
    data class EmailChanged(val email: String): UIEvent()
    data class PhoneChanged(val phone: String): UIEvent()
    data class PasswordChanged(val password: String): UIEvent()

    object RegisterButtonClicked : UIEvent()
}