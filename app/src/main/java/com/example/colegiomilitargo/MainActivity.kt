package com.seuprojeto.colegioMilitarGO

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.logincompose.screens.CadastrarScreen
import com.example.logincompose.screens.HomeScreen
import com.example.logincompose.screens.LoginScreen
import com.google.firebase.FirebaseApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)

        setContent {
            MaterialTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "login") {
                    composable("login") { LoginScreen(navController) }
                    composable("cadastrar") { CadastrarScreen(navController) }
                    composable("home") { HomeScreen(navController) }
                }
            }
        }
    }
}
