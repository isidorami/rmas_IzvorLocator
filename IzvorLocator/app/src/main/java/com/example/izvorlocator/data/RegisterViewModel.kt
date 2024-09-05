package com.example.izvorlocator.data

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.izvorlocator.app.AppRouter
import com.example.izvorlocator.app.Screen
import com.example.izvorlocator.data.validation.Validator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.AuthStateListener


class RegisterViewModel : ViewModel(){

    private var tag = RegisterViewModel::class.simpleName

    var registerUIState = mutableStateOf(RegisterUIState())

    var allValidationsPassed = mutableStateOf(false)

    fun onEvent(event: RegisterUIEvent){

        when(event){
            is RegisterUIEvent.FirstnameChanged -> {
                registerUIState.value = registerUIState.value.copy(
                    firstname = event.firstname
                )
                val firstnameResult = Validator.validateFirstname(registerUIState.value.firstname)
                registerUIState.value = registerUIState.value.copy(
                    firstnameError = firstnameResult.status)
            }
            is RegisterUIEvent.LastnameChanged -> {
                registerUIState.value = registerUIState.value.copy(
                    lastname = event.lastname
                )
                val lastnameResult = Validator.validateLastname(registerUIState.value.lastname)
                registerUIState.value = registerUIState.value.copy(
                    lastnameError = lastnameResult.status)
            }
            is RegisterUIEvent.EmailChanged -> {
                registerUIState.value = registerUIState.value.copy(
                    email = event.email
                )
                val emailResult = Validator.validateEmail(registerUIState.value.email)
                registerUIState.value = registerUIState.value.copy(
                    emailError = emailResult.status
                )
            }
            is RegisterUIEvent.PhoneChanged -> {
                registerUIState.value = registerUIState.value.copy(
                    phone = event.phone
                )
                val phoneResult = Validator.validatePhone(registerUIState.value.phone)
                registerUIState.value = registerUIState.value.copy(
                    phoneError = phoneResult.status
                )
            }
            is RegisterUIEvent.PasswordChanged -> {
                registerUIState.value = registerUIState.value.copy(
                    password = event.password
                )
                val passwordResult = Validator.validatePassword(registerUIState.value.password)
                registerUIState.value = registerUIState.value.copy(
                    passwordError = passwordResult.status
                )
            }
            is RegisterUIEvent.RegisterButtonClicked -> {
                register()
            }
        }
        allValidationsPassed.value = validateAll()
    }

    private fun register(){
        if(allValidationsPassed.value){
            createUserInFirebase(
                email = registerUIState.value.email,
                password = registerUIState.value.password
            )
        }
    }
    private fun validateAll(): Boolean {
        val pom = registerUIState.value
        return (pom.firstnameError && pom.lastnameError
            && pom.emailError && pom.phoneError && pom.passwordError)
    }

    private fun createUserInFirebase(email:String, password: String){
        FirebaseAuth.getInstance()
            .createUserWithEmailAndPassword(email, password)
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

    fun logout(){
        val firebaseAuth = FirebaseAuth.getInstance()
        firebaseAuth.signOut()
        val authStateListener = AuthStateListener{
            if(it.currentUser == null){
                Log.d(tag, "Inside Logout")
                AppRouter.emptyStack()
                AppRouter.navigateTo(Screen.LoginScreen)
            }
        }
        firebaseAuth.addAuthStateListener(authStateListener)
    }

    fun deleteAccount() {
        val firebaseAuth = FirebaseAuth.getInstance()
        val currentUser = firebaseAuth.currentUser

        currentUser?.let { user ->
            user.delete()
                .addOnCompleteListener { deleteTask ->
                    if (deleteTask.isSuccessful) {
                        Log.d(tag, "User account deleted.")
                        AppRouter.navigateTo(Screen.LoginScreen)
                    } else {
                        Log.e(tag, "Account deletion failed.")
                    }
                }
        } ?: run {
            Log.e(tag, "No user is currently signed in.")
        }
    }
}