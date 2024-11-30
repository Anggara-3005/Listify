package com.example.listify.screen.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.listify.model.AuthState
import com.example.listify.model.AuthViewModel
import com.example.listify.screen.parts.Logo
import kotlinx.coroutines.delay

@Composable
fun SplashPage(navController: NavController, authViewModel: AuthViewModel) {
    // Observe the authentication state
    val authState by authViewModel.authState.observeAsState()

    // Delay and navigation logic
    LaunchedEffect(authState) {
        // Wait for 5 seconds
        delay(3000)

        when (authState) {
            is AuthState.Authenticated -> {
                navController.navigate("home") {
                    popUpTo("splash") { inclusive = true }
                }
            }
            is AuthState.Unauthenticated -> {
                navController.navigate("login") {
                    popUpTo("splash") { inclusive = true }
                }
            }
            else -> { /* Do nothing if loading or error */ }
        }
    }

    // Splash screen layout
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF13002E), Color(0xFF13002E))
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            // Logo Image
            Logo()

            // Spacer to add space between logo and text
            Spacer(modifier = Modifier.height(16.dp))

            // Circular loading indicator
            CircularProgressIndicator(
                color = Color.White,
                strokeWidth = 4.dp
            )
        }
    }
}
