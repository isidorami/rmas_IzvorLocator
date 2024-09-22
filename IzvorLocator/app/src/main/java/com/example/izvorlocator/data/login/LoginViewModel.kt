package com.example.izvorlocator.data.login

import android.app.Dialog
import android.util.Log
import android.view.Window
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.izvorlocator.app.AppRouter
import com.example.izvorlocator.app.Screen
import com.example.izvorlocator.data.validation.Validator
import com.google.firebase.auth.FirebaseAuth

class LoginViewModel: ViewModel() {
    var loginUIState = mutableStateOf(LoginUIState())

    var allValidationsPassed = mutableStateOf(false)

    var isLoading = mutableStateOf(false)

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
        isLoading.value = true
        FirebaseAuth.getInstance()
            .signInWithEmailAndPassword(email, password)
            .addOnCompleteListener{
                Log.d("proba","LOGIN -- IN COMPLETE LISTENER")
                Log.d("proba","${it.isSuccessful}")
                if(it.isSuccessful){
                    isLoading.value = false
                    AppRouter.navigateTo(Screen.MapScreen)
                }
            }
            .addOnFailureListener{
                isLoading.value = false
                Log.d("proba","LOGIN -- IN FAILURE LISTENER")
                Log.d("proba","${it.message}")
            }
    }
    private fun validateAll(): Boolean {
        val pom = loginUIState.value
        return (pom.emailError && pom.passwordError)
    }

}