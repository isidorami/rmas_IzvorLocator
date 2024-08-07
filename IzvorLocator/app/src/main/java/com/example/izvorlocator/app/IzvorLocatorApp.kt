package com.example.izvorlocator.app

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.izvorlocator.screens.LoginScreen
import com.example.izvorlocator.screens.SignUpScreen

@Composable
fun IzvorLocatorApp(){
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
        ) {
        Crossfade(targetState = AppRouter.currentScreen, label = "") { currentState->
            when(currentState.value){
                is Screen.SignUpScreen -> {
                    SignUpScreen()
                }
                is Screen.LoginScreen -> {
                    LoginScreen()
                }
            }
        }
    }
}