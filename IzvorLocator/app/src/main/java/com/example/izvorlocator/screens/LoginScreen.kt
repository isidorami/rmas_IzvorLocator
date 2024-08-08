package com.example.izvorlocator.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.izvorlocator.R
import com.example.izvorlocator.app.AppRouter
import com.example.izvorlocator.app.Screen
import com.example.izvorlocator.components.ButtonComponent
import com.example.izvorlocator.components.ClickableLoginTextComponent
import com.example.izvorlocator.components.DividerTextComponent
import com.example.izvorlocator.components.HeadingTextComponent
import com.example.izvorlocator.components.NormalTextComponent
import com.example.izvorlocator.components.PasswordTextFieldComponent
import com.example.izvorlocator.components.TextFieldComponent
import com.example.izvorlocator.components.UnderLinedTextComponent

@Composable
fun LoginScreen(){
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(28.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            NormalTextComponent(value = stringResource(id = R.string.greeting))
            HeadingTextComponent(value = stringResource(id = R.string.login_naslov))
            Spacer(modifier = Modifier.height(40.dp))
            TextFieldComponent(
                labelValue = stringResource(id = R.string.email),
                painterResource(id = R.drawable.email),
                onTextChanged = {

                }
            )
            PasswordTextFieldComponent(
                labelValue = stringResource(id = R.string.password),
                painterResource(id = R.drawable.lock),
                onTextChanged = {

                }
            )
            Spacer(modifier = Modifier.height(10.dp))
            UnderLinedTextComponent(value = stringResource(id = R.string.zaborav))
            Spacer(modifier = Modifier.height(70.dp))
            ButtonComponent(value = stringResource(id = R.string.login),
                onButtonClicked = {

                })
            Spacer(modifier = Modifier.height(30.dp))
            DividerTextComponent()
            Spacer(modifier = Modifier.height(20.dp))
            ClickableLoginTextComponent(
                question = stringResource(id = R.string.nemas)+" "
                , text = stringResource(id = R.string.register)+"!"
                , onTextSelected = {
                    AppRouter.navigateTo(Screen.SignUpScreen)
                })
        }
    }
}

@Preview
@Composable
fun DefaultPreviewOfLoginScreen(){
    LoginScreen()
}