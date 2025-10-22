package com.example.essence.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.essence.R
import com.example.essence.ui.screens.Screen


@Composable
fun BottomBar(
    modifier: Modifier = Modifier,
    selectedScreen: Screen,
    onItemSelected: (Screen) -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(64.dp)
            .background(MaterialTheme.colorScheme.surface),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        BottomBarItem(
            screen = Screen.Dashboard,
            label = "Home",
            icon = R.drawable.icon_dashboard,
            isSelected = selectedScreen == Screen.Dashboard,
            onClick = onItemSelected,
            modifier = Modifier.weight(1f)
        )
        BottomBarItem(
            screen = Screen.Schedule,
            label = "Schedule",
            icon = R.drawable.icon_leave,
            isSelected = selectedScreen == Screen.Schedule,
            onClick = onItemSelected,
            modifier = Modifier.weight(1f)
        )
        BottomBarItem(
            screen = Screen.Payslip,
            label = "Payslip",
            icon = R.drawable.icon_payslip,
            isSelected = selectedScreen == Screen.Payslip,
            onClick = onItemSelected,
            modifier = Modifier.weight(1f)
        )
        BottomBarItem(
            screen = Screen.Profile,
            label = "Profile",
            icon = R.drawable.icon_profile,
            isSelected = selectedScreen == Screen.Profile,
            onClick = onItemSelected,
            modifier = Modifier.weight(1f)
        )
    }
}


@Composable
fun BottomBarItem(
    screen: Screen,
    label: String,
    icon: Int,
    isSelected: Boolean,
    onClick: (Screen) -> Unit,
    modifier: Modifier
) {
    val selectedColor = MaterialTheme.colorScheme.primary
    val unselectedColor = MaterialTheme.colorScheme.onSurfaceVariant

    Column(
        modifier = modifier
            .fillMaxHeight()
            .clickable { onClick(screen) }
            .background(
                if (isSelected) selectedColor.copy(alpha = 0.1f) else Color.Transparent,
                RoundedCornerShape(0.dp)
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = label,
            tint = if (isSelected) selectedColor else unselectedColor,
            modifier = Modifier.size(24.dp)
        )
//        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            color = if (isSelected) selectedColor else unselectedColor,
            fontSize = 12.sp
        )
    }
}
