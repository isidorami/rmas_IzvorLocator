package com.example.izvorlocator.app

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

sealed class Screen {
    object RegisterScreen : Screen()
    object LoginScreen : Screen()
    object MapScreen : Screen()
}

object AppRouter{
    var currentScreen: MutableState<Screen> = mutableStateOf(Screen.RegisterScreen)
    fun navigateTo(destination: Screen){
        currentScreen.value = destination
    }
}