package org.example.project.kmmchat.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel: MainViewModel by inject()
        setContent {
            val destination by viewModel.destination.collectAsStateWithLifecycle()
            val userId by viewModel.userId.collectAsStateWithLifecycle()
            MainNavigation(destination = destination, userId = userId)
        }
    }
}
