package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.components.ScreenLogin
import com.example.myapplication.components.ScreenSignUp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NavApp()
        }
    }
}

enum class Screens {
    ScreenLogin,
    ScreenSignUp,
    ScreenMap
}

@Composable
fun NavApp(modifier: Modifier = Modifier.fillMaxSize()) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screens.ScreenLogin.name) {
        composable(Screens.ScreenLogin.name) {
            ScreenLogin(
                modifier = modifier,
                navigateToSignUp = {
                    navController.navigate(Screens.ScreenSignUp.name)
                },
                onLogin = { username, password -> { }
                }
            )
        }
        composable(Screens.ScreenSignUp.name) {
            ScreenSignUp(
                modifier = modifier,
                navigateToLogin = {
                    navController.popBackStack(Screens.ScreenLogin.name, false)
                },
                onSignUp = {
                        username, password, firstName, lastName, phoneNumber -> { }
                }
            )
        }
    }
}
