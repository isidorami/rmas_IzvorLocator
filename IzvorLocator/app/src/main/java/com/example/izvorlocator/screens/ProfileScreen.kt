package com.example.izvorlocator.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberImagePainter
import com.example.izvorlocator.R
import com.example.izvorlocator.components.ButtonComponent
import com.example.izvorlocator.components.HeadingTextComponent
import com.example.izvorlocator.components.NormalTextComponent
import com.example.izvorlocator.data.user.UserViewModel
import com.example.izvorlocator.components.ProfileInfoRow

@Composable
fun ProfileScreen(userViewModel: UserViewModel = viewModel()) {

    // Data fetching happens only once when the screen is first displayed
    LaunchedEffect(Unit) {
        userViewModel.fetchUser()
    }

    val userState = userViewModel.userUIState.value
    val imageUri = userViewModel.imageUri.value

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(28.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            //HeadingTextComponent(value = stringResource(id = R.string.moj_profil))
            Spacer(modifier = Modifier.height(20.dp))
            Box(modifier = Modifier
                .fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ){
                    Image(
                        painter = rememberImagePainter(data = imageUri),
                        contentDescription = "Profile Photo",
                        modifier = Modifier
                            .size(150.dp)
                            .clip(CircleShape)
                    )
                }
            }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    NormalTextComponent(value = "${userState.firstname} ${userState.lastname}")

                    Spacer(modifier = Modifier.height(10.dp))
                    ProfileInfoRow(icon = Icons.Default.Email, description = userState.email)

                    Spacer(modifier = Modifier.height(10.dp))
                    ProfileInfoRow(icon = Icons.Default.Phone, description = userState.phone)

                    Spacer(modifier = Modifier.height(10.dp))
                    ProfileInfoRow(icon = Icons.Default.Star, description = "Sakupljeni poeni: ${userState.points}")
                }

            Spacer(modifier = Modifier.height(20.dp))
            ButtonComponent(
                value = stringResource(id = R.string.delete),
                onButtonClicked = { userViewModel.deleteAccount() },
                isEnabled = true
            )
        }
    }
}

@Preview
@Composable
fun DefaultPreviewOfProfileScreen(){
    ProfileScreen()
}