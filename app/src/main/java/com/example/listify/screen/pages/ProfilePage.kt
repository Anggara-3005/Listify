package com.example.listify.screen.pages

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.listify.R
import com.example.listify.model.AuthViewModel
import com.example.listify.model.TaskViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfilePage(
    modifier: Modifier = Modifier,
    navController: NavController,
    authViewModel: AuthViewModel,
    taskViewModel: TaskViewModel = viewModel()
) {
    val username by authViewModel.username.observeAsState("")
    val profileImageUri by authViewModel.profileImageUri.observeAsState(null)
    var newUsername by remember { mutableStateOf(username) }
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    val imageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            imageUri = it
            authViewModel.uploadProfileImage(it)
        }
    }

    val vintageFont = FontFamily(
        Font(R.font.playfull, FontWeight.Normal)
    )
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF3D79F8), Color(0xFF13002E))
                )
            ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(35.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "Profile",
                fontSize = 40.sp,
                fontFamily = vintageFont,
                color = Color.White,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(Color.Gray)
                    .clickable { imageLauncher.launch("image/*") },
                contentAlignment = Alignment.Center
            ) {
                if (imageUri != null || profileImageUri != null) {
                    val painter: Painter = rememberAsyncImagePainter(model = imageUri ?: profileImageUri)
                    Image(
                        painter = painter,
                        contentDescription = "Profile Image",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.size(120.dp)
                    )
                } else {
                    Icon(
                        painter = painterResource(id = R.drawable.profile),
                        contentDescription = "Add Photo",
                        modifier = Modifier.size(48.dp),
                        tint = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = newUsername,
                onValueChange = { newUsername = it },
                label = { Text("Username") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.White.copy(alpha = 0.8f),
                    cursorColor = Color.Black,
                    selectionColors = TextSelectionColors(
                        handleColor = Color.Black,
                        backgroundColor = Color.Gray.copy(alpha = 0.4f)
                    ),
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedLabelColor = Color.Black, // Warna label saat fokus
                    unfocusedLabelColor = Color.Black, // Warna label saat tidak fokus
                    focusedTextColor = Color.Black, // Warna teks input saat fokus
                    unfocusedTextColor = Color.Black // Warna teks input saat tidak fokus
                ),
                shape = RoundedCornerShape(16.dp)
            )

            // Menambahkan Row untuk tombol Save Changes dan Sign Out
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween // Mengatur jarak antar tombol
            ) {
                Button(
                    onClick = {
                        authViewModel.updateProfile(newUsername)
                        navController.navigate("home")
                    },
                    modifier = Modifier
                        .weight(1f) // Membagi ruang secara proporsional
                        .padding(end = 8.dp), // Jarak antar tombol
                    colors = ButtonDefaults.buttonColors(Color(0xFF38A1F3))
                ) {
                    Text("Save Changes")
                }

                Button(
                    onClick = {
                        authViewModel.signout()
                        navController.navigate("login") {
                            popUpTo("profile") { inclusive = true }
                        }
                    },
                    modifier = Modifier
                        .weight(1f) // Membagi ruang secara proporsional
                        .padding(start = 8.dp), // Jarak antar tombol
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) {
                    Text("Sign Out")
                }
            }
        }
    }
}


