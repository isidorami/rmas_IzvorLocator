package com.example.izvorlocator.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.example.izvorlocator.data.user.UserPhotoUIState
import com.example.izvorlocator.ui.theme.Secondary

@Composable
fun UserList(users: List<UserPhotoUIState>) {
    LazyColumn {
        itemsIndexed(users) { index, user ->
            UserItem(user = user, position = index + 1)
        }
    }
}

@Composable
fun UserItem(user: UserPhotoUIState, position: Int) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(color = Color.White, shape = RoundedCornerShape(8.dp))
            .border(2.dp, color = Secondary, shape = RoundedCornerShape(8.dp)),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = rememberImagePainter(data = user.profilePhotoUri),
                contentDescription = "User Profile Picture",
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .border(2.dp, color = Secondary, shape = CircleShape) // Border around the profile picture
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp)
            ) {
                Text(
                    text = "${user.firstname} ${user.lastname}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(4.dp))
                ProfileInfoRow(icon = Icons.Default.Email, description = user.email, fontSize = 12.sp)
                ProfileInfoRow(icon = Icons.Default.Phone, description = user.phone, fontSize = 12.sp)
                ProfileInfoRow(icon = Icons.Default.Star, description = "Sakupljeni poeni: ${user.points}", fontSize = 12.sp)
            }

            Text(
                text = "$position.",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = Color.Gray
            )
        }
    }
}