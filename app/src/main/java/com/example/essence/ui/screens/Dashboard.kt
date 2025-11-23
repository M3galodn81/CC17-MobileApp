package com.example.essence.ui.screens

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.rememberCoroutineScope
import com.example.essence.ui.components.NavigationDrawerContent
import com.example.essence.data.model.PayslipData
import com.example.essence.functions.formatTitleCaseWithSpaces
import com.example.essence.ui.components.dashboard.BottomBar
import com.example.essence.ui.components.dashboard.TopBar
import kotlinx.coroutines.launch
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import com.example.essence.LocalUserRole
import com.example.essence.data.local.SessionManager
import com.example.essence.data.model.UserRole
import com.example.essence.sendTestNotification
import com.example.essence.ui.components.RoleBasedContent
import com.example.essence.ui.components.dashboar.GlobalLoadingOverlay

enum class Screen { Dashboard,
                    Schedule,
                    Leave,
                    Payslip, PayslipDetails,
                    Profile,
                    Notification,  }



@Composable
fun screenModifier(): Modifier {
    return Modifier
        .fillMaxSize()
        .padding(16.dp)
        .verticalScroll(rememberScrollState())
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DashboardScreen(modifier: Modifier,navController : NavController) {
    // Check current screen
    var selectedScreen by remember { mutableStateOf(Screen.Dashboard) }
    var selectedPayslipData by remember { mutableStateOf<PayslipData?>(null) }

    // Check drawer status
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var isLoading by remember { mutableStateOf(false) }

    fun switchScreen(screen: Screen) {
        if (isLoading || selectedScreen == screen) return

        scope.launch {
            isLoading = true
            kotlinx.coroutines.delay(500) // simulate loading delay (optional)
            selectedScreen = screen
            isLoading = false
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .background(MaterialTheme.colorScheme.surface)
            ) {
                NavigationDrawerContent(
                    onClose = { scope.launch { drawerState.close() } },
                    onItemSelected = { screen ->
                        switchScreen(screen)
                    },
                    navController = navController
                )
            }
        }
    ) {
        Column(modifier = modifier.fillMaxSize()) {

            TopBar(
                name = formatTitleCaseWithSpaces(selectedScreen.name),
                onMenuClick =
                    if (selectedScreen == Screen.PayslipDetails)
                        null
                    else {
                        { scope.launch { drawerState.open() } }
                    }
                ,
                onNotificationClick = {
                    switchScreen(Screen.Notification)
                },
                onBackClick = if (selectedScreen == Screen.PayslipDetails) {
                    { switchScreen(Screen.Payslip) }
                } else null,
            )

            Box(
                modifier = Modifier
                    .weight(1f)
                    .background(MaterialTheme.colorScheme.background)
            ) {
                AnimatedContent(
                    targetState = selectedScreen,
                    transitionSpec = {
                        fadeIn(animationSpec = tween(300)) togetherWith
                                fadeOut(animationSpec = tween(300))
                    },
                    label = "ScreenTransition"
                ) { screen ->
                    when (screen) {
                        Screen.Dashboard -> DashboardContent()
                        Screen.Schedule -> ScheduleContent()
                        Screen.Leave -> LeaveContent()
                        Screen.Payslip -> PayslipContent(
                            onPayslipSelected = {
                                selectedPayslipData = it
                                switchScreen(Screen.PayslipDetails)
                            }
                        )
                        Screen.PayslipDetails -> {
                            selectedPayslipData?.let { payslip ->
                                PayslipDetailsScreen(payslip)
                            }
                        }
                        Screen.Profile -> ProfileContent()
                        Screen.Notification -> NotificationContent()

                        else -> DashboardContent()
                    }
                }
                if (isLoading) {
                    GlobalLoadingOverlay()
                }
            }

            BottomBar(
                selectedScreen = selectedScreen,
                isLoading = isLoading,
                onItemSelected = { screen ->
                    switchScreen(screen)
                }
            )
        }
    }
}

@Composable
fun DashboardContent() {
    Column(
        modifier = screenModifier(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // --- 1. Header ---

        Text(
            text = "Dashboard",
            style = MaterialTheme.typography.headlineLarge
        )

        val userRole = LocalUserRole.current
        Text(
            text = "Your access level: $userRole",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary
        )


        // --- 2. Role-Based Widgets ---

        RoleBasedContent(
            adminContent = { AdminDashboard() },
            managerContent = { ManagerDashboard() },
            employeeContent = { EmployeeDashboard() }
        )

    }
}

// --- Specific Dashboard UIs ---

@Composable
fun EmployeeDashboard() {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text(
            text = "Your Tools",
            style = MaterialTheme.typography.titleMedium
        )
        DashboardCard(title = "My Schedule", content = "View your upcoming shifts.")
        DashboardCard(title = "My Payslips", content = "Access your pay history.")
        DashboardCard(title = "File a Leave", content = "Submit a request for time off.")
    }
}

@Composable
fun ManagerDashboard() {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        // Managers see their own tools first
        EmployeeDashboard()

        // Then they see manager-specific tools
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Manager Tools",
            style = MaterialTheme.typography.titleMedium
        )
        DashboardCard(title = "Team Schedule", content = "View your team's calendar.")
        DashboardCard(title = "Approve Requests", content = "Manage leave and overtime.")
    }
}

@Composable
fun AdminDashboard() {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        // Admins see all tools
        ManagerDashboard()

        // Then they see admin-specific tools
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Admin Tools",
            style = MaterialTheme.typography.titleMedium
        )
        DashboardCard(title = "Manage Users", content = "Add or remove employees.")
        DashboardCard(title = "System Settings", content = "Configure app-wide settings.")

        val context  =  LocalContext.current
        // Add the test button here
        Button(onClick = { sendTestNotification(context) }) {
            Text("Send Test Notification")
        }
    }
}

/**
 * A simple, reusable Card for dashboard items.
 */
@Composable
fun DashboardCard(title: String, content: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = content,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}