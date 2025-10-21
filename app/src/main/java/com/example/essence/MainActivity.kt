package com.example.essence



import com.example.essence.R;
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.MailOutline
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.essence.ui.theme.ESSenceTheme
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ESSenceTheme {
                // The NavController will manage switching between the Login and Dashboard screens
                AppNavigation()
            }
        }
    }
}

// VALUES
val notificationCount = 3


// --- Navigation Composable (The core of the multi-screen app) ---

@Composable
fun AppNavigation() {
    val navController = rememberNavController()


    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Routes.DASHBOARD, // Start the app at the Login screen
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Routes.LOGIN) {
                // Using LoginScreen
                LoginScreen(navController = navController)
            }

            composable(Routes.REGISTRATION) {
                // Using LoginScreen
                RegistrationScreen(navController = navController)
            }
            composable(Routes.DASHBOARD) {
                // Using DashboardScreen
                DashboardScreen(
                    modifier = Modifier.fillMaxSize()
                        .background(Color.White)
                )
            }
        }
    }
}