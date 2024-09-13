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
import kotlinx.coroutines.*

class UserViewModel : ViewModel(){

    private var tag = UserViewModel::class.simpleName

    private lateinit var databaseReference: DatabaseReference
    private lateinit var storageReference: StorageReference
    var isLoading = mutableStateOf(false)

    private val _allUsersList = mutableStateOf<List<UserPhotoUIState>>(emptyList())
    val allUsersList: State<List<UserPhotoUIState>> = _allUsersList

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
        isLoading.value = true
        val firebaseAuth = FirebaseAuth.getInstance()
        val currentUser = firebaseAuth.currentUser

        currentUser?.let { user ->
            user.delete()
                .addOnCompleteListener { deleteTask ->
                    if (deleteTask.isSuccessful) {
                        Log.d(tag, "User account deleted.")
                        isLoading.value = false
                        clearUserData()
                        AppRouter.navigateTo(Screen.RegisterScreen)
                    } else {
                        Log.e(tag, "Account deletion failed.")
                        isLoading.value = false
                    }
                }
        } ?: run {
            Log.e(tag, "No user is currently signed in.")
            isLoading.value = false
        }
    }

    fun fetchUser(){
        isLoading.value = true
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
                        fetchProfilePhoto(uid){ uri ->
                            // Update the user object with their profile photo URL
                            imageUri.value = uri
                        }
                    }
                    isLoading.value = false
                }
                override fun onCancelled(error: DatabaseError) {
                    Log.d(tag,"ON CANCELLED -> DATABASE ERROR")
                    isLoading.value = false
                }
            })
        }
    }

    fun fetchProfilePhoto(uid: String, onPhotoFetched: (Uri?) -> Unit) {
        storageReference = FirebaseStorage.getInstance().reference.child("Users/$uid")

        storageReference.downloadUrl
            .addOnSuccessListener { uri ->
                Log.d("UserViewModel", "FETCH PHOTO SUCCESS")
                onPhotoFetched(uri)
            }
            .addOnFailureListener {
                Log.d("UserViewModel", "FETCH PHOTO FAILURE")
                onPhotoFetched(null) // If there's no photo, return null
            }
    }

    fun fetchAllUsers() {
        isLoading.value = true
        val databaseReference = FirebaseDatabase.getInstance().getReference("Users")

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val userList = mutableListOf<UserPhotoUIState>()
                val fetchTasks = mutableListOf<Deferred<Unit>>() // To keep track of photo fetching

                val coroutineScope = CoroutineScope(Dispatchers.IO)

                for (userSnapshot in snapshot.children) {
                    val user = userSnapshot.getValue(UserPhotoUIState::class.java)
                    user?.let {
                        userList.add(it)
                        val uid = userSnapshot.key.toString()

                        // Create a CompletableDeferred for the photo fetch
                        val photoDeferred = CompletableDeferred<Unit>()

                        // Fetch profile photo asynchronously
                        fetchProfilePhoto(uid) { uri ->
                            it.profilePhotoUri = uri
                            photoDeferred.complete(Unit) // Complete the deferred
                        }

                        // Add the deferred to the list of fetch tasks
                        fetchTasks.add(photoDeferred)
                    }
                }

                // Launch a coroutine to wait for all fetch tasks to complete
                coroutineScope.launch {
                    fetchTasks.awaitAll() // Await all photo fetch tasks

                    // Update the list after all photos are fetched
                    withContext(Dispatchers.Main) {
                        _allUsersList.value = userList.sortedByDescending { it.points }
                        Log.d(tag, "Updated user list with photos: $_allUsersList")
                        isLoading.value = false
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Log.d(tag, "ON CANCELLED -> DATABASE ERROR")
                isLoading.value = false
            }
        })
    }
    fun addPointsToUser(additionalPoints: Int) {
        val auth = FirebaseAuth.getInstance()
        val uid = auth.currentUser?.uid.toString()

        val databaseReference = FirebaseDatabase.getInstance().getReference("Users")

        if (uid.isNotEmpty()) {
            databaseReference.child(uid).child("points").get().addOnSuccessListener { snapshot ->
                val currentPoints = snapshot.getValue(Int::class.java) ?: 0

                val newPoints = currentPoints + additionalPoints

                val updates = mapOf<String, Any>("points" to newPoints)

                databaseReference.child(uid).updateChildren(updates)
                    .addOnSuccessListener {
                        Log.d("proba", "Poeni uspešno dodati! Novi broj poena: $newPoints")
                    }
                    .addOnFailureListener { error ->
                        Log.e("proba", "Dodavanje poena nije uspelo: ${error.message}")
                    }
            }.addOnFailureListener { error ->
                Log.e("proba", "Greška pri preuzimanju poena: ${error.message}")
            }
        }
    }

}