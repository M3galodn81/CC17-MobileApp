package com.example.essence.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.essence.LocalUserRole
import com.example.essence.data.model.PayslipData
import com.example.essence.functions.formatTitleCaseWithSpaces
import com.example.essence.sendTestNotification
import com.example.essence.ui.components.NavigationDrawerContent
import com.example.essence.ui.components.RoleBasedContent
import com.example.essence.ui.components.dashboar.GlobalLoadingOverlay
import com.example.essence.ui.components.dashboard.BottomBar
import com.example.essence.ui.components.dashboard.TopBar
import kotlinx.coroutines.launch

enum class Screen {
    Dashboard,
    Schedule,
    Leave,
    Payslip, PayslipDetails,
    Profile,
    Notification,
    PayrollProcessing // Added for Payroll Officer flow
}

@Composable
fun screenModifier(): Modifier {
    return Modifier
        .fillMaxSize()
        .padding(16.dp)
        .verticalScroll(rememberScrollState())
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DashboardScreen(modifier: Modifier, navController: NavController) {
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
                    },
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
                        Screen.Dashboard -> DashboardContent(
                            onNavigate = { target -> switchScreen(target) }
                        )
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

                        // Placeholder for the new Payroll Processing screen
                        Screen.PayrollProcessing -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text("Payroll Processing Screen")
                        }

                        else -> DashboardContent(
                            onNavigate = { target -> switchScreen(target) }
                        )
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
fun DashboardContent(onNavigate: (Screen) -> Unit) {
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
        // We check if the string representation of the role matches "PayrollOfficer"
        // If RoleBasedContent doesn't support a specific 'payroll' slot, we handle it here.

        if (userRole.toString() == "PayrollOfficer") {
            PayrollOfficerDashboard(onNavigate)
        } else {
            RoleBasedContent(
                adminContent = { AdminDashboard(onNavigate) },
                managerContent = { ManagerDashboard(onNavigate) },
                employeeContent = { EmployeeDashboard(onNavigate) },
                payrollContent = {PayrollOfficerDashboard(onNavigate)}
            )
        }
    }
}

// --- Specific Dashboard UIs ---

@Composable
fun PayrollOfficerDashboard(onNavigate: (Screen) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {

        // Quick Stats Section for Payroll Officer
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            StatsCard(
                title = "Next Pay Date",
                value = "Oct 30",
                modifier = Modifier.weight(1f)
            )
            StatsCard(
                title = "Pending Issues",
                value = "4",
                modifier = Modifier.weight(1f),
                isAlert = true
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Payroll Management",
            style = MaterialTheme.typography.titleMedium
        )

        DashboardCard(
            title = "Process Payroll",
            content = "Run the bi-weekly payroll batch calculation.",
            onClick = { onNavigate(Screen.PayrollProcessing) }
        )
        DashboardCard(
            title = "Verify Timesheets",
            content = "Review approved overtime and shift differentials.",
            onClick = { onNavigate(Screen.Schedule) }
        )
        DashboardCard(
            title = "Tax & Compliance",
            content = "Generate BIR 2316 and other tax reports.",
            onClick = { onNavigate(Screen.Notification) }
        )
        DashboardCard(
            title = "Employee Salary Adjustments",
            content = "Manage bonuses, deductions, and basic pay rates.",
            onClick = { onNavigate(Screen.Profile) }
        )
    }
}

@Composable
fun EmployeeDashboard(onNavigate: (Screen) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text(
            text = "Your Tools",
            style = MaterialTheme.typography.titleMedium
        )
        DashboardCard(
            title = "My Schedule",
            content = "View your upcoming shifts.",
            onClick = { onNavigate(Screen.Schedule) }
        )
        DashboardCard(
            title = "My Payslips",
            content = "Access your pay history.",
            onClick = { onNavigate(Screen.Payslip) }
        )
        DashboardCard(
            title = "File a Leave",
            content = "Submit a request for time off.",
            onClick = { onNavigate(Screen.Leave) }
        )
    }
}

@Composable
fun ManagerDashboard(onNavigate: (Screen) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        // Managers see their own tools first
        EmployeeDashboard(onNavigate)

        // Then they see manager-specific tools
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Manager Tools",
            style = MaterialTheme.typography.titleMedium
        )
        DashboardCard(
            title = "Team Schedule",
            content = "View your team's calendar.",
            onClick = { onNavigate(Screen.Schedule) }
        )
        DashboardCard(
            title = "Approve Requests",
            content = "Manage leave and overtime.",
            onClick = { onNavigate(Screen.Leave) }
        )
    }
}

@Composable
fun AdminDashboard(onNavigate: (Screen) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        // Admins see all tools
        ManagerDashboard(onNavigate)

        // Then they see admin-specific tools
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Admin Tools",
            style = MaterialTheme.typography.titleMedium
        )
        DashboardCard(
            title = "Manage Users",
            content = "Add or remove employees.",
            onClick = { onNavigate(Screen.Profile) } // Mapped to Profile for now
        )
        DashboardCard(
            title = "System Settings",
            content = "Configure app-wide settings.",
            onClick = { onNavigate(Screen.Notification) } // Placeholder mapping
        )

        val context = LocalContext.current
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
fun DashboardCard(title: String, content: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick), // Make the entire card clickable
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

/**
 * A mini card for displaying statistics (e.g., Next Pay Date).
 */
@Composable
fun StatsCard(title: String, value: String, modifier: Modifier = Modifier, isAlert: Boolean = false) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = if (isAlert) MaterialTheme.colorScheme.errorContainer else MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium,
                color = if (isAlert) MaterialTheme.colorScheme.onErrorContainer else MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = value,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = if (isAlert) MaterialTheme.colorScheme.onErrorContainer else MaterialTheme.colorScheme.onSurface
            )
        }
    }
}