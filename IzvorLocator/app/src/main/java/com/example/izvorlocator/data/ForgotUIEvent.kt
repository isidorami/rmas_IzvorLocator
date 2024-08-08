package com.example.izvorlocator.data

sealed class ForgotUIEvent {
    data class EmailChanged(val email: String): ForgotUIEvent()
    object ForgotButtonClicked : ForgotUIEvent()
}