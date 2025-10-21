package com.example.essence

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Composable
fun NavigationDrawerContent(
    onClose: () -> Unit,
    onItemSelected: (Screen) -> Unit
) {
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

        Text("Section 2", modifier = Modifier.padding(16.dp), style = MaterialTheme.typography.titleMedium)
        NavigationDrawerItem(
            label = { Text("Settings") },
            selected = false,
            icon = { Icon(Icons.Outlined.Settings, contentDescription = null) },
            badge = { Text("20") }, // Placeholder
            onClick = { /* Handle click */ }
        )
        NavigationDrawerItem(
            label = { Text("Help and feedback") },
            selected = false,
            icon = { Icon(Icons.Outlined.Settings, contentDescription = null) },
            onClick = { /* Handle click */ },
        )
        Spacer(Modifier.height(12.dp))
    }
}
