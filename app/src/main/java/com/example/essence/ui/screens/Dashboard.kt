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
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.example.essence.data.local.SessionManager
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
        modifier = screenModifier()
    ) {
        repeat(50) {
            Text("Dashboard item #$it", color = MaterialTheme.colorScheme.onBackground)
        }
    }
}







