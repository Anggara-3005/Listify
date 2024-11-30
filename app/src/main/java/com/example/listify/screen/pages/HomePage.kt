package com.example.listify.screen.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.listify.R
import com.example.listify.model.AuthViewModel
import com.example.listify.model.TaskViewModel
import com.example.listify.screen.parts.TaskItem
import com.example.listify.screen.parts.ProfileSection

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePage(
    modifier: Modifier = Modifier,
    navController: NavController,
    authViewModel: AuthViewModel,
    taskViewModel: TaskViewModel = viewModel()
) {
    val authState = authViewModel.authState.observeAsState()
    val username by authViewModel.username.observeAsState("User")
    var newTaskTitle by remember { mutableStateOf("") }
    val tasks by taskViewModel.tasks.observeAsState(emptyList())
    val vintageFont = FontFamily(
        Font(R.font.playfull, FontWeight.Normal)
    )
    var searchQuery by remember { mutableStateOf("") }
    var showCompletedTasks by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) } // State untuk menampilkan pop-up "Add Task"
    var showSearchDialog by remember { mutableStateOf(false) } // State untuk menampilkan pop-up "Search Bar"

    LaunchedEffect(Unit) {
        taskViewModel.fetchTasks()
    }

    LaunchedEffect(authState.value) {
        if (authState.value is com.example.listify.model.AuthState.Unauthenticated) {
            navController.navigate("login") {
                popUpTo("home") { inclusive = true }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF3D79F8), Color(0xFF13002E))
                )
            )
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header Section
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Listify",
                    fontSize = 40.sp,
                    fontFamily = vintageFont,
                    color = Color.White,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                ProfileSection(vintageFont = vintageFont, navController = navController)
            }

            Text(
                text = "Hello, $username!",
                fontSize = 24.sp,
                fontFamily = vintageFont,
                modifier = Modifier.padding(bottom = 24.dp),
                color = Color.White
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween, // Memastikan "Task List" di kiri dan tombol di kanan
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (showCompletedTasks) "Completed Tasks" else "Task List",
                    fontSize = 24.sp,
                    fontFamily = vintageFont,
                    color = Color.White
                )

                IconButton(
                    onClick = { showSearchDialog = true }, // Menampilkan pop-up untuk Search Bar
                    modifier = Modifier.size(60.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_search_24),
                        contentDescription = "Search Tasks",
                        tint = Color.White
                    )
                }
            }

            val filteredTasks = tasks.filter {
                it.title.contains(searchQuery, ignoreCase = true) &&
                        (if (showCompletedTasks) it.completed else true)
            }

            Box(
                modifier = Modifier
                    .height(360.dp)
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    items(filteredTasks) { task ->
                        TaskItem(
                            task = task,
                            onDelete = { taskViewModel.deleteTask(task.id) },
                            onCompletionChange = { isCompleted ->
                                taskViewModel.updateTaskCompletion(task.id, isCompleted)
                            }
                        )
                    }
                }
            }

            // Add Button
            IconButton(
                onClick = { showDialog = true },
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .size(60.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_add_24),
                    contentDescription = "Add Task",
                    tint = Color.White,
                    modifier = Modifier.size(60.dp)
                )
            }

            // Pop-up Dialog for Adding Task
            if (showDialog) {
                Dialog(onDismissRequest = { showDialog = false }) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White, shape = RoundedCornerShape(16.dp))
                            .padding(16.dp)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            TextField(
                                value = newTaskTitle,
                                onValueChange = { newTaskTitle = it },
                                label = { Text("New Task") },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 8.dp),
                                colors = TextFieldDefaults.textFieldColors(
                                    containerColor = Color.LightGray,
                                    cursorColor = Color.Black,
                                    focusedLabelColor = Color.Black, // Warna label saat fokus
                                    unfocusedLabelColor = Color.Black, // Warna label saat tidak fokus
                                    focusedTextColor = Color.Black, // Teks input saat fokus
                                    unfocusedTextColor = Color.Black // Teks input saat tidak fokus
                                )
                            )



                            Button(
                                onClick = {
                                    if (newTaskTitle.isNotBlank()) {
                                        taskViewModel.addTask(newTaskTitle)
                                        newTaskTitle = "" // Clear the input
                                    }
                                    showDialog = false // Tutup pop-up
                                },
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(Color(0xFF38A1F3))
                            ) {
                                Text("Save")
                            }
                        }
                    }
                }
            }

            // Pop-up Dialog for Search Bar
            if (showSearchDialog) {
                Dialog(onDismissRequest = { showSearchDialog = false }) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White, shape = RoundedCornerShape(16.dp))
                            .padding(16.dp)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            TextField(
                                value = searchQuery,
                                onValueChange = { searchQuery = it },
                                label = { Text("Search Tasks") },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 8.dp),
                                colors = TextFieldDefaults.textFieldColors(
                                    containerColor = Color.LightGray,
                                    cursorColor = Color.Black,
                                    focusedLabelColor = Color.Black, // Warna label saat fokus
                                    unfocusedLabelColor = Color.Black, // Warna label saat tidak fokus
                                    focusedTextColor = Color.Black, // Warna teks input saat fokus
                                    unfocusedTextColor = Color.Black // Warna teks input saat tidak fokus
                                )
                            )

                            Button(
                                onClick = {
                                    showSearchDialog = false // Tutup pop-up setelah pencarian
                                },
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(Color(0xFF38A1F3))
                            ) {
                                Text("Close")
                            }
                        }
                    }
                }
            }
        }
    }
}
