package com.example.izvorlocator.data.forgot

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.izvorlocator.app.AppRouter
import com.example.izvorlocator.app.Screen
import com.example.izvorlocator.data.validation.Validator
import com.google.firebase.auth.FirebaseAuth

class ForgotViewModel : ViewModel() {
    private var tag = ForgotViewModel::class.simpleName

    var forgotUIState = mutableStateOf(ForgotUIState())

    var allValidationsPassed = mutableStateOf(false)

    fun onEvent(event: ForgotUIEvent){
        when(event){
            is ForgotUIEvent.EmailChanged -> {
                forgotUIState.value = forgotUIState.value.copy(
                    email = event.email
                )
                val emailResult = Validator.validateEmail(forgotUIState.value.email)
                forgotUIState.value = forgotUIState.value.copy(
                    emailError = emailResult.status
                )
            }
            is ForgotUIEvent.ForgotButtonClicked -> {
                if(forgotUIState.value.emailError){
                    forgotPassword()
                }
            }
        }
        allValidationsPassed = mutableStateOf(forgotUIState.value.emailError)
    }
    private fun forgotPassword(){
        val email = forgotUIState.value.email
        FirebaseAuth.getInstance()
            .sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(tag, "Email sent.")
                    AppRouter.navigateTo(Screen.LoginScreen)
                }
            }
            .addOnFailureListener{
                Log.d(tag, "In failure listener")
            }
    }
}