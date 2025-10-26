package com.example.essence

import android.Manifest
import android.annotation.SuppressLint
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
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.essence.data.local.SessionManager
import com.example.essence.data.model.UserRole

// VALUES
var notificationCount = 789
const val CRASH_COUNTER = 1

val LocalUserRole = compositionLocalOf<UserRole> {
    error("No UserRole provided")
}
class MainActivity : ComponentActivity() {

    // --- Permission Launcher ---
    // We must ask for permission to post notifications on API 33+
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            // Permission is granted. You can now send notifications.
        } else {
            // Permission denied. Handle this gracefully (e.g., show a message)
        }
    }

    private fun askNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // Directly ask for the permission
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        createNotificationChannel()
        askNotificationPermission() // Ask for permission on startup
        setContent {
            val context = LocalContext.current
            var userRole by remember {
                mutableStateOf(SessionManager.getUserRole(context))
            }

            CompositionLocalProvider(LocalUserRole provides userRole) {
                ESSenceTheme {
                    AppNavigation(
                        onLoginSuccess = { newRole ->
                            userRole = newRole
                        },
                    )
                }
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


}

fun sendTestNotification(context: Context) {
    // We must check if permission is granted BEFORE sending (for API 33+)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        if (ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Cannot send notification, permission is not granted
            return
        }
    }

    val builder = NotificationCompat.Builder(context, "test_channel")
        .setSmallIcon(R.drawable.ic_launcher_foreground)
        .setContentTitle("Test Notification")
        .setContentText("This is a test notification from ESSence.")
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)

    with(NotificationManagerCompat.from(context)) {

        @SuppressLint("MissingPermission")
        notify(1001, builder.build())
    }
}

// --- Navigation Composable (The core of the multi-screen app) ---
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavigation(
    onLoginSuccess: (UserRole) -> Unit
) {
    val navController = rememberNavController()

    val context = LocalContext.current
    val startDestination = if (SessionManager.isLoggedIn(context)) {
        Routes.DASHBOARD
    } else {
        Routes.LOGIN
    }

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Routes.LOGIN) {
                // Using LoginScreen
                LoginScreen(navController = navController,
                    onLoginSuccess = onLoginSuccess)
            }

//            composable(Routes.REGISTRATION) {
//                // Using LoginScreen
//                RegistrationScreen(navController = navController)
//            }
            composable(Routes.DASHBOARD) {
                // Using DashboardScreen
                DashboardScreen(
                    navController = navController,
                    modifier = Modifier.fillMaxSize()
                        .background(Color.White)
                )
            }
        }
    }
}