package com.example.listify.screen.parts

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.listify.R

@Composable

fun ProfileSection(vintageFont: androidx.compose.ui.text.font.FontFamily, navController: NavController) {
    // Button to navigate to ProfilePage
    IconButton(
        onClick = {
            // Navigate to ProfilePage
            navController.navigate("finish")
        }
    ) {
        Icon(
            painter = painterResource(id = R.drawable.baseline_account_circle_24), // Replace with your profile picture resource
            contentDescription = "Go to Profile Page",
            tint = Color.White,
            modifier = Modifier.size(100.dp)
        )
    }
}