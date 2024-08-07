package com.example.izvorlocator.data

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class LoginViewModel : ViewModel(){

    var TAG = LoginViewModel::class.simpleName

    var registrationUIState = mutableStateOf(RegistrationUIState())

    fun onEvent(event: UIEvent){
        when(event){
            is UIEvent.FirstnameChanged -> {
                registrationUIState.value = registrationUIState.value.copy(
                    firstname = event.firstname
                )
            }
            is UIEvent.LastnameChanged -> {
                registrationUIState.value = registrationUIState.value.copy(
                    lastname = event.lastname
                )
            }
            is UIEvent.EmailChanged -> {
                registrationUIState.value = registrationUIState.value.copy(
                    email = event.email
                )
            }
            is UIEvent.PhoneChanged -> {
                registrationUIState.value = registrationUIState.value.copy(
                    phone = event.phone
                )
            }
            is UIEvent.PasswordChanged -> {
                registrationUIState.value = registrationUIState.value.copy(
                    password = event.password
                )
            }
            is UIEvent.RegisterButtonClicked -> {
                signUp()
            }
        }
    }

    private fun signUp() {
        TODO("validacija")
        Log.d(TAG, "Printanje stateova")
        Log.d(TAG, registrationUIState.value.toString())
    }
}