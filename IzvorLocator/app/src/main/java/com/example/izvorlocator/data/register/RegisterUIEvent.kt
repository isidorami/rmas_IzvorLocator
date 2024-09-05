package com.example.izvorlocator.data.register

sealed class RegisterUIEvent {
    data class FirstnameChanged(val firstname: String): RegisterUIEvent()
    data class LastnameChanged(val lastname: String): RegisterUIEvent()
    data class EmailChanged(val email: String): RegisterUIEvent()
    data class PhoneChanged(val phone: String): RegisterUIEvent()
    data class PasswordChanged(val password: String): RegisterUIEvent()

    object RegisterButtonClicked : RegisterUIEvent()
}