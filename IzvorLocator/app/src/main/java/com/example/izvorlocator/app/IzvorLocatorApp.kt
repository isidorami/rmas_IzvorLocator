package com.example.izvorlocator.app

import android.content.Context
import android.content.Intent
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.izvorlocator.data.location.LocationService
import com.example.izvorlocator.data.pois.PoiViewModel
import com.example.izvorlocator.screens.users.ForgotPasswordScreen
import com.example.izvorlocator.screens.users.LoginScreen
import com.example.izvorlocator.screens.NavScreen
import com.example.izvorlocator.screens.users.RegisterScreen

@Composable
fun IzvorLocatorApp(poiViewModel: PoiViewModel,
                    context: Context,
                    startLocationService: Intent,
                    stopLocationService: Intent){
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
        ) {
        Crossfade(targetState = AppRouter.currentScreen, label = "") { currentState->
            when(currentState.value){
                Screen.RegisterScreen -> {
                    RegisterScreen()
                }
                Screen.LoginScreen -> {
                    LoginScreen()
                }
                Screen.ForgotPasswordScreen -> {
                    ForgotPasswordScreen()
                }
                else -> {
                    NavScreen(poiViewModel = poiViewModel,
                        context = context,
                        startLocationService,
                        stopLocationService)
                }
            }
        }
        BackHandler {
            AppRouter.popBackStack()
        }
    }
}