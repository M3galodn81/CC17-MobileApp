package com.example.essence.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.essence.ui.components.RoleBasedContent // Assumed available
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.UUID

// --- Data Models ---

enum class LeaveStatus {
    PENDING, APPROVED, REJECTED
}

data class LeaveRequest(
    val id: String = UUID.randomUUID().toString(),
    val employeeName: String,
    val startDate: LocalDate,
    val endDate: LocalDate,
    val reason: String,
    val status: LeaveStatus = LeaveStatus.PENDING
)

// --- Main Screen ---

@Composable
fun LeaveContent() {
    // Shared State (Simulating a database)
    var leaveRequests by remember {
        mutableStateOf(
            listOf(
                LeaveRequest(
                    employeeName = "Alice Johnson",
                    startDate = LocalDate.now().plusDays(10),
                    endDate = LocalDate.now().plusDays(12),
                    reason = "Family wedding",
                    status = LeaveStatus.PENDING
                ),
                LeaveRequest(
                    employeeName = "Bob Smith",
                    startDate = LocalDate.now().plusDays(15),
                    endDate = LocalDate.now().plusDays(16),
                    reason = "Medical appointment",
                    status = LeaveStatus.APPROVED
                )
            )
        )
    }

    val onSubmitRequest: (LeaveRequest) -> Unit = { newRequest ->
        leaveRequests = leaveRequests + newRequest
    }

    val onStatusChange: (String, LeaveStatus) -> Unit = { id, newStatus ->
        leaveRequests = leaveRequests.map {
            if (it.id == id) it.copy(status = newStatus) else it
        }
    }

    // Use RoleBasedContent to determine what view to show
    RoleBasedContent(
        adminContent = {
            // Admins get the full Manager/Team view
            ManagerLeavePortal(
                requests = leaveRequests,
                onStatusChange = onStatusChange,
                onSelfApply = onSubmitRequest
            )
        },
        managerContent = {
            // Managers get the full Manager/Team view
            ManagerLeavePortal(
                requests = leaveRequests,
                onStatusChange = onStatusChange,
                onSelfApply = onSubmitRequest
            )
        },
        employeeContent = {
            // Regular employees just see the apply screen
            Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                EmployeeApplyScreen(onSubmit = onSubmitRequest)
            }
        },
        payrollContent =
            {
                Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                    EmployeeApplyScreen(onSubmit = onSubmitRequest)
                }
            }
    )
}

// --- Manager Portal (Tabs: Apply vs Manage) ---

@Composable
fun ManagerLeavePortal(
    requests: List<LeaveRequest>,
    onStatusChange: (String, LeaveStatus) -> Unit,
    onSelfApply: (LeaveRequest) -> Unit
) {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("My Application", "Team Requests")

    Column(modifier = Modifier.fillMaxSize()) {
        TabRow(selectedTabIndex = selectedTab) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = { Text(title) }
                )
            }
        }

        Box(modifier = Modifier.weight(1f).padding(16.dp)) {
            when (selectedTab) {
                0 -> EmployeeApplyScreen(onSubmit = onSelfApply)
                1 -> TeamLeaveList(requests = requests, onStatusChange = onStatusChange)
            }
        }
    }
}

// --- Employee Apply Form ---

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmployeeApplyScreen(onSubmit: (LeaveRequest) -> Unit) {
    var reason by remember { mutableStateOf("") }
    var showDatePicker by remember { mutableStateOf(false) }
    var selectedStartDate by remember { mutableStateOf<LocalDate?>(null) }

    // Validation Logic
    val today = LocalDate.now()
    val minAllowedDate = today.plusWeeks(1)

    val isDateValid = selectedStartDate != null && !selectedStartDate!!.isBefore(minAllowedDate)
    val isReasonValid = reason.isNotBlank()
    val canSubmit = isDateValid && isReasonValid

    var showSuccessMessage by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Text("Apply for Leave", style = MaterialTheme.typography.headlineSmall)
        Text(
            "Note: Applications must be submitted at least 1 week in advance.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.secondary
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Date Picker Trigger
        OutlinedCard(
            onClick = { showDatePicker = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.DateRange, contentDescription = null)
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text("Start Date", style = MaterialTheme.typography.labelMedium)
                    Text(
                        text = selectedStartDate?.format(DateTimeFormatter.ISO_DATE) ?: "Select Date",
                        style = MaterialTheme.typography.bodyLarge,
                        color = if (selectedStartDate == null) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }

        if (selectedStartDate != null && !isDateValid) {
            Text(
                text = "Error: Start date must be at least 7 days from today.",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 4.dp, start = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Reason Field
        OutlinedTextField(
            value = reason,
            onValueChange = { reason = it },
            label = { Text("Reason for leave (Mandatory)") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 3,
            isError = reason.isBlank() && selectedStartDate != null
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                if (canSubmit) {
                    onSubmit(
                        LeaveRequest(
                            employeeName = "Current User", // Replace with actual user name from session
                            startDate = selectedStartDate!!,
                            endDate = selectedStartDate!!.plusDays(1),
                            reason = reason
                        )
                    )
                    reason = ""
                    selectedStartDate = null
                    showSuccessMessage = true
                }
            },
            enabled = canSubmit,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Submit Request")
        }

        if (showSuccessMessage) {
            Text(
                "Request Submitted Successfully!",
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
    }

    // Date Picker Dialog
    if (showDatePicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = Instant.now().toEpochMilli()
        )

        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            selectedStartDate = Instant.ofEpochMilli(millis)
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate()
                        }
                        showDatePicker = false
                    }
                ) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) { Text("Cancel") }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}

// --- Manager List View ---

@Composable
fun TeamLeaveList(
    requests: List<LeaveRequest>,
    onStatusChange: (String, LeaveStatus) -> Unit
) {
    val pendingRequests = requests.filter { it.status == LeaveStatus.PENDING }
    val historyRequests = requests.filter { it.status != LeaveStatus.PENDING }

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            Text(
                "Pending Approvals (${pendingRequests.size})",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }

        if (pendingRequests.isEmpty()) {
            item {
                Text("No pending requests.", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
            }
        }

        items(pendingRequests) { request ->
            LeaveRequestCard(request, isManagerMode = true, onStatusChange = onStatusChange)
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                "Team History",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }

        items(historyRequests) { request ->
            LeaveRequestCard(request, isManagerMode = true, onStatusChange = null)
        }
    }
}

@Composable
fun LeaveRequestCard(
    request: LeaveRequest,
    isManagerMode: Boolean,
    onStatusChange: ((String, LeaveStatus) -> Unit)?
) {
    val statusColor = when (request.status) {
        LeaveStatus.APPROVED -> Color(0xFF4CAF50)
        LeaveStatus.REJECTED -> MaterialTheme.colorScheme.error
        LeaveStatus.PENDING -> Color(0xFFFF9800)
    }

    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(request.employeeName, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text(
                    request.status.name,
                    color = statusColor,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text("Date: ${request.startDate}", style = MaterialTheme.typography.bodyMedium)

            Spacer(modifier = Modifier.height(8.dp))

            Text("Reason:", style = MaterialTheme.typography.labelMedium, color = Color.Gray)
            Text(request.reason, style = MaterialTheme.typography.bodyMedium)

            if (isManagerMode && request.status == LeaveStatus.PENDING && onStatusChange != null) {
                Divider(modifier = Modifier.padding(vertical = 12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    OutlinedButton(
                        onClick = { onStatusChange(request.id, LeaveStatus.REJECTED) },
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error)
                    ) {
                        Text("Reject")
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Button(
                        onClick = { onStatusChange(request.id, LeaveStatus.APPROVED) },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
                    ) {
                        Text("Approve")
                    }
                }
            }
        }
    }
}