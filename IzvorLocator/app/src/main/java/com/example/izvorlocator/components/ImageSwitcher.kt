package com.example.izvorlocator.components

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.example.izvorlocator.R

@Composable
fun ImageSwitcher(imageUris: List<Uri>) {
    var currentIndex by remember { mutableStateOf(0) }
    if (currentIndex >= imageUris.size) {
        currentIndex = 0
    }
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ){
        IconButton(
            onClick = { if (currentIndex > 0) currentIndex-- else currentIndex = imageUris.size - 1},
            modifier = Modifier.size(40.dp)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "",
                modifier = Modifier.size(26.dp),
                tint = Color.Black
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Image(
            painter = rememberImagePainter(
                data = imageUris.getOrNull(currentIndex) ?: R.drawable.blank
            ),
            contentDescription = "Profile Photo",
            modifier = Modifier
                .size(200.dp)
                .clip(RectangleShape)
        )
        Spacer(modifier = Modifier.width(8.dp))
        IconButton(
            onClick = { if (currentIndex < imageUris.size - 1) currentIndex++ else currentIndex = 0},
            modifier = Modifier.size(40.dp)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = "",
                modifier = Modifier.size(26.dp),
                tint = Color.Black
            )
        }
    }
}