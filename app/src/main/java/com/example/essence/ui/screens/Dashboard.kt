package com.example.essence.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.rememberCoroutineScope
import com.example.essence.ui.components.NavigationDrawerContent
import com.example.essence.R
import com.example.essence.data.model.PayslipData
import com.example.essence.functions.formatTitleCaseWithSpaces
import com.example.essence.notificationCount
import com.example.essence.ui.components.BadgeIconOnlyFlatButton
import com.example.essence.ui.components.BottomBar
import com.example.essence.ui.components.IconOnlyFlatButton
import com.example.essence.ui.components.TopBar
import com.example.essence.ui.components.TopIconFlatButton
import kotlinx.coroutines.launch
import androidx.compose.animation.*
import androidx.compose.animation.core.tween

enum class Screen { Dashboard,
                    Schedule,
                    Payslip, PayslipDetails,
                    Profile,
                    Notification,  }

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DashboardScreen(modifier: Modifier) {
    var selectedScreen by remember { mutableStateOf(Screen.Dashboard) }
    var selectedPayslipData by remember { mutableStateOf<PayslipData?>(null) }

    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

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
                        selectedScreen = screen
                    }
                )
            }
        }
    ) {
        Column(modifier = modifier.fillMaxSize()) {

            TopBar(
                name = formatTitleCaseWithSpaces(selectedScreen.name),
                onMenuClick = {
                    if (selectedScreen == Screen.PayslipDetails) null else {
                        { scope.launch { drawerState.open() } }
                    }
                },
                onNotificationClick = {
                    selectedScreen = Screen.Notification
                },
                onBackClick = if (selectedScreen == Screen.PayslipDetails) {
                    { selectedScreen = Screen.Payslip }
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
                                selectedScreen = Screen.PayslipDetails
                            }
                        )
                        Screen.PayslipDetails -> {
                            selectedPayslipData?.let { payslip ->
                                PayslipDetailsScreen(payslip)
                            }
                        }
                        Screen.Profile -> ProfileContent()
                        Screen.Notification -> NotificationContent()
                    }
                }
            }

            BottomBar(onItemSelected = { selectedScreen = it }, selectedScreen = selectedScreen)
        }
    }
}

@Composable
fun DashboardContent() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        repeat(50) {
            Text("Dashboard item #$it", color = MaterialTheme.colorScheme.onBackground)
        }
    }
}







