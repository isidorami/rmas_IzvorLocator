package com.example.myapplication.services

import android.content.Context
import android.net.Uri
import androidx.activity.result.ActivityResultLauncher
import androidx.core.content.FileProvider
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date

class TakePhotoService(private val context: Context) {

    private var photoUri: Uri? = null
    private lateinit var takePhotoLauncher: ActivityResultLauncher<Uri>


    lateinit var currentPhotoPath: String

    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = context.getExternalFilesDir(null)
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
        }
    }
    fun setup(takePhotoLauncher: ActivityResultLauncher<Uri>) {
        this.takePhotoLauncher = takePhotoLauncher
    }

    fun takePhoto() {
        val photoFile = createImageFile()
        photoUri = FileProvider.getUriForFile(context, "com.example.myapplication.fileprovider", photoFile)
        takePhotoLauncher.launch(photoUri)
    }

    fun getPhotoUri(): Uri? {
        return photoUri
    }

}