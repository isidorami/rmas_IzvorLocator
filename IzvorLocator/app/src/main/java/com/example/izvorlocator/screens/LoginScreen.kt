package com.example.izvorlocator.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.izvorlocator.R
import com.example.izvorlocator.app.AppRouter
import com.example.izvorlocator.app.Screen
import com.example.izvorlocator.components.ButtonComponent
import com.example.izvorlocator.components.ClickableLoginTextComponent
import com.example.izvorlocator.components.CustomIndeterminateProgress
import com.example.izvorlocator.components.DividerTextComponent
import com.example.izvorlocator.components.HeadingTextComponent
import com.example.izvorlocator.components.PasswordTextFieldComponent
import com.example.izvorlocator.components.TextFieldComponent
import com.example.izvorlocator.components.UnderlinedTextComponent
import com.example.izvorlocator.data.login.LoginUIEvent
import com.example.izvorlocator.data.login.LoginViewModel

@Composable
fun LoginScreen(loginViewModel: LoginViewModel = viewModel()){

    val isLoading by remember { loginViewModel.isLoading }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(28.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            HeadingTextComponent(value = stringResource(id = R.string.login_naslov))
            Spacer(modifier = Modifier.height(40.dp))
            TextFieldComponent(
                labelValue = stringResource(id = R.string.email),
                imageVector = Icons.Default.Email,
                onTextChanged = {
                    loginViewModel.onEvent(LoginUIEvent.EmailChanged(it))
                },
                errorStatus = loginViewModel.loginUIState.value.emailError
            )
            PasswordTextFieldComponent(
                labelValue = stringResource(id = R.string.password),
                imageVector = Icons.Default.Lock,
                onTextChanged = {
                    loginViewModel.onEvent(LoginUIEvent.PasswordChanged(it))
                },
                errorStatus = loginViewModel.loginUIState.value.passwordError
            )
            Spacer(modifier = Modifier.height(10.dp))
            UnderlinedTextComponent(
                value = stringResource(id = R.string.zaborav),
                onUnderlinedTextClicked = {
                    AppRouter.navigateTo(Screen.ForgotPasswordScreen)
                })
            Spacer(modifier = Modifier.height(70.dp))
            ButtonComponent(value = stringResource(id = R.string.login),
                onButtonClicked = {
                    loginViewModel.onEvent(LoginUIEvent.LoginButtonClicked)
                },
                isEnabled = loginViewModel.allValidationsPassed.value
            )
            Spacer(modifier = Modifier.height(20.dp))
            DividerTextComponent()
            Spacer(modifier = Modifier.height(10.dp))
            ClickableLoginTextComponent(
                question = stringResource(id = R.string.nemas)+" "
                , text = stringResource(id = R.string.register)+"!"
                , onTextSelected = {
                    AppRouter.navigateTo(Screen.RegisterScreen)
                })
        }
        if(isLoading) {
            CustomIndeterminateProgress(modifier = Modifier)
        }
    }
}

@Preview
@Composable
fun DefaultPreviewOfLoginScreen(){
    LoginScreen()
}