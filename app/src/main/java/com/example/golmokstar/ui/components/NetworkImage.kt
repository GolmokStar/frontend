package com.example.golmokstar.ui.components


import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import coil.compose.AsyncImage
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.zIndex

@Composable
fun NetworkImage(photoUrl: String) {
    AsyncImage(
        model = photoUrl,
        contentDescription = "Network Image",
        contentScale = ContentScale.Crop,
        modifier = Modifier.zIndex(-1f).fillMaxSize()
    )
}