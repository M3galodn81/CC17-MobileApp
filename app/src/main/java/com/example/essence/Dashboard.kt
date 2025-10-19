package com.example.essence

import android.view.ContextThemeWrapper
import androidx.compose.ui.viewinterop.AndroidView
import android.widget.CalendarView
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection
import kotlinx.coroutines.launch

enum class Screen { Dashboard, Schedule, Payslip, Profile, Notification, Menu }

@Composable
fun DashboardScreen(modifier: Modifier) {
    var selectedScreen by remember { mutableStateOf(Screen.Dashboard) }
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
                name = selectedScreen.name,
                onMenuClick = {
                    scope.launch { drawerState.open() }
                },
                onNotificationClick = {
                    selectedScreen = Screen.Notification
                },
            )

            Box(
                modifier = Modifier
                    .weight(1f)
                    .background(MaterialTheme.colorScheme.background)
            ) {
                when (selectedScreen) {
                    Screen.Dashboard -> DashboardContent()
                    Screen.Schedule -> ScheduleContent()
                    Screen.Payslip -> PayslipContent()
                    Screen.Profile -> ProfileContent()
                    Screen.Notification -> NotificationContent()
                    else -> {}
                }
            }

            BottomBar(onItemSelected = { selectedScreen = it })
        }
    }
}

@Composable
fun TopBar(
    name: String,
    modifier: Modifier = Modifier,
    onMenuClick: () -> Unit,
    onNotificationClick: () -> Unit
)
{
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(MaterialTheme.colorScheme.primary),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        IconOnlyFlatButton( onMenuClick,
            Modifier
                .fillMaxHeight(),
            Icons.Outlined.Menu
        )
        Text(text = name,
            modifier = Modifier.padding(start = 16.dp),
            color = MaterialTheme.colorScheme.onPrimary,)

        Spacer(modifier = Modifier.weight(1f))

        IconOnlyFlatButton(onNotificationClick,
            Modifier
                .fillMaxHeight(),
            Icons.Outlined.Email
        )

    }
}


@Composable
fun BottomBar(
    modifier: Modifier = Modifier,
    onItemSelected: (Screen) -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(64.dp)
            .background(MaterialTheme.colorScheme.surface),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        TopIconFlatButton({ onItemSelected(Screen.Dashboard) },
            "Home",
            Modifier
                .weight(1f)
                .fillMaxHeight(),
            R.drawable.icon_dashboard
        )
        TopIconFlatButton({ onItemSelected(Screen.Schedule) },
            "Schedule",
            Modifier
                .weight(1f)
                .fillMaxHeight(),
            R.drawable.icon_leave
        )
        TopIconFlatButton({ onItemSelected(Screen.Payslip) },
            "Payslip",
            Modifier
                .weight(1f)
                .fillMaxHeight(),
            R.drawable.icon_payslip
        )
        TopIconFlatButton({onItemSelected(Screen.Profile)} ,
            "Profile",
            Modifier
                .weight(1f)
                .fillMaxHeight(),
            R.drawable.icon_profile
        )
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

@Composable
fun ScheduleContent() {
    var selectedDate by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())

    ) {
        CalendarWidget{
            year, month, day ->
            selectedDate = "$year-${month.toString().padStart(2, '0')}-${day.toString().padStart(2, '0')}"
        }
        repeat(50) {
            Text("Schedule entry #$it", color = MaterialTheme.colorScheme.onBackground)
        }
    }
}



@Composable
fun CalendarWidget(onDateChange: (year: Int, month: Int, day: Int) -> Unit) {
    val backgroundColor = MaterialTheme.colorScheme.secondary

    AndroidView(
        factory = { context ->
            CalendarView(ContextThemeWrapper(context, R.style.CustomCalendarView)).apply {
                setBackgroundColor(backgroundColor.toArgb())
                setOnDateChangeListener { _, year, month, day ->
                    onDateChange(year, month + 1, day)
                }
            }
        },
        modifier = Modifier.fillMaxWidth()
    )
}


@Composable
fun PayslipContent() {
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)
        .verticalScroll(rememberScrollState())
    ) {
        Text("Payslip screen (scrollable content here)")

        repeat(35) {
            Text("Payslip Number #$it", color = MaterialTheme.colorScheme.onBackground)
        }
    }
}
@Composable
fun ProfileContent() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text("Profile screen")
        repeat(35) {
            Text("Item Number #$it", color = MaterialTheme.colorScheme.onBackground)
        }
    }
}

@Composable
fun NotificationContent() {
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)
        .verticalScroll(rememberScrollState())
    ) {
        Text("Notifications")
        repeat(35) {
            Text("Item Number #$it", color = MaterialTheme.colorScheme.onBackground)
        }
    }

}





