package org.example.project.kmmchat.presentation.common

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun CircularIndicatorBox(modifier: Modifier = Modifier, isLoading:Boolean, composable: @Composable ()-> Unit) {
    Box(modifier = modifier){
        composable()
        if(isLoading){
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
    }
}