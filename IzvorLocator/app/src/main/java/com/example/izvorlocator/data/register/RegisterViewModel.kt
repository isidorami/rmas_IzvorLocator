package com.example.izvorlocator.data.register

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.izvorlocator.app.AppRouter
import com.example.izvorlocator.app.Screen
import com.example.izvorlocator.data.validation.Validator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import android.net.Uri
import androidx.compose.runtime.State
import com.example.izvorlocator.data.user.UserUIState

class RegisterViewModel : ViewModel(){

    private var tag = RegisterViewModel::class.simpleName

    var registerUIState = mutableStateOf(RegisterUIState())
    var allValidationsPassed = mutableStateOf(false)

    private lateinit var databaseReference: DatabaseReference
    private lateinit var storageReference: StorageReference

    private val _imageUri = mutableStateOf<Uri?>(null)
    val imageUri: State<Uri?> get() = _imageUri

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
                firstName = registerUIState.value.firstname,
                lastName = registerUIState.value.lastname,
                email = registerUIState.value.email,
                phone = registerUIState.value.phone,
                password = registerUIState.value.password
            )
        }
    }
    private fun validateAll(): Boolean {
        val pom = registerUIState.value
        return (pom.firstnameError && pom.lastnameError
            && pom.emailError && pom.phoneError && pom.passwordError)
    }

    private fun createUserInFirebase(firstName:String, lastName:String, email:String, phone:String, password:String){
        FirebaseAuth.getInstance()
            .createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener{
                Log.d(tag,"IN COMPLETE LISTENER -> FIREBASE")
                Log.d(tag,"${it.isSuccessful}")
                if(it.isSuccessful){
                    storeUserInDatabase(firstName,lastName,email,phone)
                }
            }
            .addOnFailureListener{
                Log.d(tag,"IN FAILURE LISTENER -> NOT CREATED USER IN FIREBASE")
                Log.d(tag,"${it.message}")
            }
    }

    private fun storeUserInDatabase(firstName:String, lastName:String, email:String, phone:String){
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        databaseReference = FirebaseDatabase.getInstance().getReference("Users")
        val userUIState = UserUIState(firstName, lastName, email, phone)

        if(uid != null) {
            databaseReference.child(uid).setValue(userUIState)
                .addOnCompleteListener{
                    Log.d(tag,"IN COMPLETE LISTENER -> USER IN DATABASE")
                    Log.d(tag,"${it.isSuccessful}")
                    if(it.isSuccessful){
                        val uriToUpload = _imageUri.value ?: Uri.parse("android.resource://com.example.izvorlocator/drawable/profile_photo")
                        uploadImageToFirebase(uriToUpload)
                    }
                }
                .addOnFailureListener{
                    Log.d(tag,"IN FAILURE LISTENER -> NOT STORED USER IN DATABASE")
                    Log.d(tag,"${it.message}")
                }
        }
    }

    fun onImagePicked(uri: Uri) {
        _imageUri.value = uri
    }

    private fun uploadImageToFirebase(imageUri: Uri) {
        storageReference = FirebaseStorage.getInstance().getReference("Users/"+FirebaseAuth.getInstance().currentUser?.uid)

        storageReference.putFile(imageUri)
            .addOnCompleteListener{
                Log.d(tag,"IMAGE UPLOAD HERE -> ${it.isSuccessful}")
                if(it.isSuccessful){
                    _imageUri.value = null
                    AppRouter.navigateTo(Screen.LoginScreen)
                }
            }
            .addOnFailureListener{
                Log.d(tag,"IMAGE NOT UPLOADED")
                Log.d(tag,"${it.message}")
            }
    }
}