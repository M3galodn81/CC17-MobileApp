package com.example.essence.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.example.essence.LocalUserRole
import com.example.essence.data.model.UserRole
import com.example.essence.data.local.SessionManager

/**
 * A "guard" composable that shows the correct content
 * based on the current user's role.
 */
@Composable
fun RoleBasedContent(
    adminContent: @Composable () -> Unit,
    managerContent: @Composable () -> Unit,
    employeeContent: @Composable () -> Unit
) {
    // Just read the role directly! No context needed.
    val role = LocalUserRole.current

    when (role) {
        UserRole.ADMIN -> adminContent()
        UserRole.MANAGER -> managerContent()
        UserRole.EMPLOYEE -> employeeContent()
    }
}