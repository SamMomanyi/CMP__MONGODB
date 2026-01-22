package org.example.project.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ErrorScreen(message: String? = null){
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(all = 24.dp)
    ){
        Text(text = message ?: "Empty")
    }
}