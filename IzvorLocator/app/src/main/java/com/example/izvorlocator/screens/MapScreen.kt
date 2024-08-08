package com.example.izvorlocator.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.izvorlocator.components.ButtonComponent
import com.example.izvorlocator.components.HeadingTextComponent
import com.example.izvorlocator.data.RegisterViewModel

@Composable
fun MapScreen(registerViewModel: RegisterViewModel = viewModel()){
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(28.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            HeadingTextComponent(value = "MAPA - TO BE DONE")

            ButtonComponent(
                value = "Izloguj se",
                onButtonClicked = { registerViewModel.logout() },
                isEnabled = true
            )
        }
    }
}

@Preview
@Composable
fun DefaultPreviewOfMapScreen(){
    MapScreen()
}