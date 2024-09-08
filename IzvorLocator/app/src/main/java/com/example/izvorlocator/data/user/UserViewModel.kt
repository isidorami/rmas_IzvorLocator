package com.example.izvorlocator.data.user

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.izvorlocator.app.AppRouter
import com.example.izvorlocator.app.Screen
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.AuthStateListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class UserViewModel : ViewModel(){

    private var tag = UserViewModel::class.simpleName

    private lateinit var databaseReference: DatabaseReference
    private lateinit var storageReference: StorageReference

    var userUIState = mutableStateOf(UserUIState())
        private set

    var imageUri = mutableStateOf<Uri?>(null)
        private set

    private fun clearUserData() {
        userUIState.value = UserUIState()
        imageUri.value = null
    }

    fun logout(){
        val firebaseAuth = FirebaseAuth.getInstance()
        firebaseAuth.signOut()
        val authStateListener = AuthStateListener{
            if(it.currentUser == null){
                Log.d(tag, "Inside Logout")
                clearUserData()

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
                        clearUserData()
                        AppRouter.navigateTo(Screen.LoginScreen)
                    } else {
                        Log.e(tag, "Account deletion failed.")
                    }
                }
        } ?: run {
            Log.e(tag, "No user is currently signed in.")
        }
    }

    fun fetchUser(){
        val auth = FirebaseAuth.getInstance()
        val uid = auth.currentUser?.uid.toString()

        databaseReference = FirebaseDatabase.getInstance().getReference("Users")
        if(uid.isNotEmpty()){
            databaseReference.child(uid).addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    Log.d(tag,"ON DATA CHANGE -> SUCCESS!")
                    val user = snapshot.getValue(UserUIState::class.java)
                    Log.d(tag, user.toString())
                    user?.let {
                        userUIState.value = it
                        fetchProfilePhoto(uid)
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                    Log.d(tag,"ON CANCELLED -> DATABASE ERROR")
                }
            })
        }
    }

    fun fetchProfilePhoto(uid: String){
        storageReference = FirebaseStorage.getInstance().reference.child("Users/${uid}")
        storageReference.downloadUrl
            .addOnSuccessListener { uri ->
                imageUri.value = uri
                Log.d("UserViewModel", "FETCH PHOTO SUCCESS")
            }
            .addOnFailureListener {
                Log.d("UserViewModel", "FETCH PHOTO FAILURE")
            }
    }
}