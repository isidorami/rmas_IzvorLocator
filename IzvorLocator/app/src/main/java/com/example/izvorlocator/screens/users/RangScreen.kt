package com.example.izvorlocator.screens.users

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.izvorlocator.components.CustomIndeterminateProgress
import com.example.izvorlocator.data.user.UserList
import com.example.izvorlocator.data.user.UserViewModel

@Composable
fun RangScreen(userViewModel: UserViewModel = viewModel()) {
    val isLoading by remember { userViewModel.isLoading }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(28.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {

            LaunchedEffect(Unit) {
                userViewModel.fetchAllUsers()
            }

            val sortedUserList = userViewModel.allUsersList.value

            UserList(users = sortedUserList)
        }
        if(isLoading) {
            CustomIndeterminateProgress(modifier = Modifier)
        }
    }
}

@Preview
@Composable
fun DefaultPreviewOfRangScreen(){
    RangScreen()
}