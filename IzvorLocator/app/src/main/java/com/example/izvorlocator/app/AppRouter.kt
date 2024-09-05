package com.example.izvorlocator.app

import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

sealed class Screen {
    object RegisterScreen : Screen()
    object LoginScreen : Screen()
    object ForgotPasswordScreen : Screen()
    object MapScreen : Screen()
    object RangScreen : Screen()
    object ListScreen : Screen()
}

object AppRouter{
    private val screenStack = mutableListOf<Screen>(Screen.RegisterScreen)
    var currentScreen: MutableState<Screen> = mutableStateOf(Screen.RegisterScreen)

    fun navigateTo(destination: Screen) {
        screenStack.add(destination)
        currentScreen.value = destination
    }

    fun popBackStack() {
        if (screenStack.size > 1) {
            screenStack.removeAt(screenStack.size - 1)
            currentScreen.value = screenStack.last()
        }
        else{
            System.exit(0);
        }
    }
    fun emptyStack() {
        while(screenStack.size > 1) {
            screenStack.removeAt(screenStack.size - 1)
        }
    }
}

@Composable
fun BackHandler(enabled: Boolean = true, onBack: () -> Unit) {
    val backCallback = remember {
        object : OnBackPressedCallback(enabled) {
            override fun handleOnBackPressed() {
                onBack()
            }
        }
    }

    val backDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher

    DisposableEffect(backDispatcher) {
        backDispatcher?.addCallback(backCallback)
        onDispose { backCallback.remove() }
    }
}