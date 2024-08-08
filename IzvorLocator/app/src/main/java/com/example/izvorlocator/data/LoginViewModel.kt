package com.example.izvorlocator.data

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.izvorlocator.data.validation.Validator
import com.google.firebase.auth.FirebaseAuth

class LoginViewModel : ViewModel(){

    var TAG = LoginViewModel::class.simpleName

    var registrationUIState = mutableStateOf(RegistrationUIState())

    fun onEvent(event: UIEvent){

        when(event){
            is UIEvent.FirstnameChanged -> {
                registrationUIState.value = registrationUIState.value.copy(
                    firstname = event.firstname
                )
                val firstnameResult = Validator.validateFirstname(registrationUIState.value.firstname)
                registrationUIState.value = registrationUIState.value.copy(
                    firstnameError = firstnameResult.status)
            }
            is UIEvent.LastnameChanged -> {
                registrationUIState.value = registrationUIState.value.copy(
                    lastname = event.lastname
                )
                val lastnameResult = Validator.validateLastname(registrationUIState.value.lastname)
                registrationUIState.value = registrationUIState.value.copy(
                    lastnameError = lastnameResult.status)
            }
            is UIEvent.EmailChanged -> {
                registrationUIState.value = registrationUIState.value.copy(
                    email = event.email
                )
                val emailResult = Validator.validateEmail(registrationUIState.value.email)
                registrationUIState.value = registrationUIState.value.copy(
                    emailError = emailResult.status
                )
            }
            is UIEvent.PhoneChanged -> {
                registrationUIState.value = registrationUIState.value.copy(
                    phone = event.phone
                )
                val phoneResult = Validator.validatePhone(registrationUIState.value.phone)
                registrationUIState.value = registrationUIState.value.copy(
                    phoneError = phoneResult.status
                )
            }
            is UIEvent.PasswordChanged -> {
                registrationUIState.value = registrationUIState.value.copy(
                    password = event.password
                )
                val passwordResult = Validator.validatePassword(registrationUIState.value.password)
                registrationUIState.value = registrationUIState.value.copy(
                    passwordError = passwordResult.status
                )
            }
            is UIEvent.RegisterButtonClicked -> {
                signUp()
            }
        }
    }

    private fun signUp(){
        if(validateAll()){
            createUserInFirebase(
                email = registrationUIState.value.email,
                password = registrationUIState.value.password
                    )
        }
    }
    private fun validateAll(): Boolean {
        val pom = registrationUIState.value
        return (pom.firstnameError && pom.lastnameError
            && pom.emailError && pom.phoneError && pom.passwordError)
    }
    private fun print() {
        Log.d(TAG, "Printanje register")
        Log.d(TAG, registrationUIState.value.toString())
    }

    private fun createUserInFirebase(email:String, password: String){
        FirebaseAuth.getInstance()
            .createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener{
                Log.d(TAG,"IN COMPLETE LISTENER")
                Log.d(TAG,"${it.isSuccessful}")
            }
            .addOnFailureListener{
                Log.d(TAG,"IN FAILURE LISTENER")
                Log.d(TAG,"${it.message}")
            }
    }
}