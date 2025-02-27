package com.example.golmokstar.ui.components


import androidx.compose.runtime.Composable
import coil.compose.AsyncImage
import androidx.compose.ui.layout.ContentScale

@Composable
fun NetworkImage(photoUrl: String) {
    AsyncImage(
        model = photoUrl,
        contentDescription = "Network Image",
        contentScale = ContentScale.Crop
    )
}