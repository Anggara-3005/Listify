package com.example.listify

import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.listify.model.AuthViewModel
import com.example.listify.model.Task
import com.example.listify.screen.pages.HomePage
import com.example.listify.screen.pages.LoginPage
import com.example.listify.screen.pages.ProfilePage
import com.example.listify.screen.pages.SignupPage
import com.example.listify.screen.pages.SplashPage
import com.example.listify.screen.parts.TaskItem


@Composable
fun AppNavigation(modifier: Modifier = Modifier, authViewModel: AuthViewModel) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "splash", builder = {
        // MyNavigation.kt
//
        composable("splash") {
            SplashPage(navController = navController, authViewModel)
        }
//
        composable("login") {
            LoginPage(modifier, navController, authViewModel)
        }
        composable("signup") {
            SignupPage(modifier, navController, authViewModel)
        }
        composable("home") {
            HomePage(modifier, navController, authViewModel)
        }
        composable("finish") {
            ProfilePage(modifier, navController, authViewModel)

        }



    })
}