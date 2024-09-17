package com.example.izvorlocator.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.unit.dp
import com.example.izvorlocator.ui.theme.Secondary

//@Composable
//fun CustomIndeterminateProgress(modifier: Modifier)
//{
//    val infiniteTransition = rememberInfiniteTransition(label = "")
//    val animatedProgress by infiniteTransition.animateFloat(
//        initialValue = 0f,
//        targetValue = 1f,
//        animationSpec = infiniteRepeatable(
//            animation = tween(durationMillis = 1200, easing = LinearEasing),
//            repeatMode = RepeatMode.Restart
//        ), label = ""
//    )
//
//    Box(
//        modifier = modifier
//            .fillMaxSize()
//            .clickable { false },
//        contentAlignment = Alignment.Center
//    ) {
//        Card(
//            modifier = Modifier.size(100.dp)
//        ) {
//            Box(
//                modifier = Modifier
//                    .size(100.dp)
//                    .padding(16.dp),
//                contentAlignment = Alignment.Center
//            ) {
//                CircularProgressIndicator(
//                    progress = animatedProgress,
//                    modifier = Modifier.size(48.dp),
//                    color = Secondary
//                )
//            }
//        }
//    }
//}

@Composable
fun CustomIndeterminateProgress(modifier: Modifier) {
    val infiniteTransition = rememberInfiniteTransition(label = "")

    // Define the progress animation
    val animatedProgress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = ""
    )

    // Define the rotation animation
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = ""
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .clickable { false },
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier.size(100.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                // CircularProgressIndicator with rotation
                CircularProgressIndicator(
                    progress = animatedProgress,
                    modifier = Modifier
                        .size(48.dp)
                        .rotate(rotation),
                    color = Secondary
                )
            }
        }
    }
}