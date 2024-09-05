package com.example.izvorlocator.app

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.example.izvorlocator.R
import com.example.izvorlocator.screens.ForgotPasswordScreen
import com.example.izvorlocator.screens.LoginScreen
import com.example.izvorlocator.screens.NavScreen
import com.example.izvorlocator.screens.RegisterScreen

@Composable
fun IzvorLocatorApp(){
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
        ) {
        Crossfade(targetState = AppRouter.currentScreen, label = "") { currentState->
            when(currentState.value){
                is Screen.RegisterScreen -> {
                    RegisterScreen()
                }
                is Screen.LoginScreen -> {
                    LoginScreen()
                }
                is Screen.ForgotPasswordScreen -> {
                    ForgotPasswordScreen()
                }
                is Screen.MapScreen -> {
                    NavScreen(currentScreen = stringResource(R.string.mapa))
                }
                is Screen.RangScreen -> {
                    NavScreen(currentScreen = stringResource(R.string.rangiranje_korisnika))
                }
                is Screen.ListScreen -> {
                    NavScreen(currentScreen = stringResource(R.string.lista_izvora))
                }
            }
        }
        BackHandler {
            AppRouter.popBackStack()
        }
    }
}