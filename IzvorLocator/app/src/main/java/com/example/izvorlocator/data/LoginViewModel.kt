package com.example.izvorlocator.data

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.izvorlocator.app.AppRouter
import com.example.izvorlocator.app.Screen
import com.example.izvorlocator.data.validation.Validator
import com.google.firebase.auth.FirebaseAuth

class LoginViewModel: ViewModel() {
    private var tag = LoginViewModel::class.simpleName

    var loginUIState = mutableStateOf(LoginUIState())

    var allValidationsPassed = mutableStateOf(false)

    fun onEvent(event: LoginUIEvent){

        when(event){
            is LoginUIEvent.EmailChanged -> {
                loginUIState.value = loginUIState.value.copy(
                    email = event.email
                )
                val emailResult = Validator.validateEmail(loginUIState.value.email)
                loginUIState.value = loginUIState.value.copy(
                    emailError = emailResult.status
                )
            }
            is LoginUIEvent.PasswordChanged -> {
                loginUIState.value = loginUIState.value.copy(
                    password = event.password
                )
                val passwordResult = Validator.validatePassword(loginUIState.value.password)
                loginUIState.value = loginUIState.value.copy(
                    passwordError = passwordResult.status
                )
            }
            is LoginUIEvent.LoginButtonClicked -> {
                login()
            }
        }
        allValidationsPassed.value = validateAll()
    }

    private fun login(){
        if(allValidationsPassed.value){
            loginUserWithFirebase(
                email = loginUIState.value.email,
                password = loginUIState.value.password
            )
        }
    }
    private fun loginUserWithFirebase(email:String, password: String){
        FirebaseAuth.getInstance()
            .signInWithEmailAndPassword(email, password)
            .addOnCompleteListener{
                Log.d(tag,"IN COMPLETE LISTENER")
                Log.d(tag,"${it.isSuccessful}")
                if(it.isSuccessful){
                    AppRouter.navigateTo(Screen.MapScreen)
                }
            }
            .addOnFailureListener{
                Log.d(tag,"IN FAILURE LISTENER")
                Log.d(tag,"${it.message}")
            }
    }
    private fun validateAll(): Boolean {
        val pom = loginUIState.value
        return (pom.emailError && pom.passwordError)
    }
}