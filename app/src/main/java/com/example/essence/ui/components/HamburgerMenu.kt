package com.example.essence.ui.components

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.essence.CRASH_COUNTER
import com.example.essence.MainActivity
import com.example.essence.R
import com.example.essence.ui.screens.Screen

@Composable
fun NavigationDrawerContent(
    onClose: () -> Unit,
    onItemSelected: (Screen) -> Unit
) {
    val context = LocalContext.current

    Column(
        modifier = Modifier.padding(horizontal = 16.dp)
            .width(304.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(Modifier.height(12.dp))
        Text("ESSence", modifier = Modifier.padding(16.dp), style = MaterialTheme.typography.titleLarge)
        HorizontalDivider()

        Text("General", modifier = Modifier.padding(16.dp), style = MaterialTheme.typography.titleMedium)
        NavigationDrawerItem(
            label = { Text("Dashboard") },
            selected = false,
            onClick = { onClose()
                onItemSelected(Screen.Dashboard) },
            icon = {Icon(painterResource(R.drawable.icon_dashboard), contentDescription = null) },
        )
        NavigationDrawerItem(
            label = { Text("Schedule") },
            selected = false,
            onClick = { onClose()
                onItemSelected(Screen.Schedule) },
            icon = {Icon(painterResource(R.drawable.icon_leave), contentDescription = null) },
        )
        NavigationDrawerItem(
            label = { Text("Payslip") },
            selected = false,
            onClick = { onClose()
                onItemSelected(Screen.Payslip) },
            icon = {Icon(painterResource(R.drawable.icon_payslip), contentDescription = null) },
        )
        NavigationDrawerItem(
            label = { Text("Profile") },
            selected = false,
            onClick = { onClose()
                onItemSelected(Screen.Profile) },
            icon = {Icon(painterResource(R.drawable.icon_profile), contentDescription = null) },
        )

        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

        Text("Developer Tools", modifier = Modifier.padding(16.dp), style = MaterialTheme.typography.titleMedium)
        NavigationDrawerItem(
            label = { Text("Crash Counter") },
            selected = false,
            icon = { Icon(Icons.Outlined.Settings, contentDescription = null) },
            badge = { Text("$CRASH_COUNTER") }, // Placeholder
            onClick = { Toast.makeText(context, "Yes, I somehow crash it since Nov. 31 doesn't exist", Toast.LENGTH_SHORT).show()}
        )
        NavigationDrawerItem(
            label = { Text("Notification Test") },
            selected = false,
            icon = { Icon(Icons.Outlined.Settings, contentDescription = null) },
            onClick = { onClose()
                if (context is MainActivity) {
                    context.sendTestNotification()
                } },
        )
        Spacer(Modifier.height(12.dp))
    }
}
