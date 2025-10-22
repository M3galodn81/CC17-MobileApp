package com.example.essence



import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.essence.ui.theme.ESSenceTheme
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.essence.ui.screens.DashboardScreen
import com.example.essence.ui.screens.LoginScreen
import com.example.essence.ui.screens.RegistrationScreen
import com.example.essence.ui.screens.Routes
import java.time.LocalDate

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat.getSystemService


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        createNotificationChannel()
        setContent {
            ESSenceTheme {
                // The NavController will manage switching between the Login and Dashboard screens
                AppNavigation()
            }
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Test Notifications"
            val descriptionText = "Channel for test notifications"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("test_channel", name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
    fun sendTestNotification() {
        val builder = NotificationCompat.Builder(this, "test_channel")
            .setSmallIcon(R.drawable.ic_launcher_foreground) // Replace with your own icon
            .setContentTitle("Test Notification")
            .setContentText("This is a test notification from ESSence.")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(this)) {
            notify(1001, builder.build())
        }
    }


}

// VALUES
val notificationCount = 789


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