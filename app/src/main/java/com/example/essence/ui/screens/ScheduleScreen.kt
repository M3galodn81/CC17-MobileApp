package com.example.essence.ui.screens

import android.widget.CalendarView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

// --- Data Models ---
data class Shift(
    val id: String,
    val employeeId: String,
    val start: LocalDateTime,
    val end: LocalDateTime,
    val role: String? = null
)

data class EmployeeSchedule(
    val employeeId: String,
    val employeeName: String,
    val shifts: List<Shift>
)

// --- Sample Data for Previewing ---
val sampleShifts = listOf(
    Shift("1", "E1", LocalDateTime.now().withHour(9).withMinute(0), LocalDateTime.now().withHour(17).withMinute(0), "Manager"),
    Shift("2", "E1", LocalDateTime.now().plusDays(1).withHour(10).withMinute(0), LocalDateTime.now().plusDays(1).withHour(18).withMinute(0), "Cashier")
)

@Composable
fun ScheduleContent() {
    // State: Default to today
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }

    // Filter Logic: Find shifts that fall on the selected date
    val shiftsForDay = remember(selectedDate) {
        sampleShifts.filter { shift ->
            shift.start.toLocalDate() == selectedDate
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp) // Replaced undefined screenModifier()
    ) {
        Text(
            text = "Select Date",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // 1. Calendar View
        Card(
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            CalendarWidget { year, month, day ->
                selectedDate = LocalDate.of(year, month, day)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Schedule for ${selectedDate.format(DateTimeFormatter.ofPattern("MMM dd, yyyy"))}",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // 2. Shift List (Using LazyColumn for scrolling)
        if (shiftsForDay.isEmpty()) {
            Box(modifier = Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                Text("No shifts scheduled.", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.weight(1f) // Takes up remaining screen space
            ) {
                items(shiftsForDay) { shift ->
                    ShiftItemCard(shift)
                }
            }
        }
    }
}

@Composable
fun ShiftItemCard(shift: Shift) {
    val timeFormatter = DateTimeFormatter.ofPattern("h:mm a")

    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = shift.role ?: "Employee",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "ID: ${shift.employeeId}",
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "${shift.start.format(timeFormatter)} - ${shift.end.format(timeFormatter)}",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                )
                // Calculate duration
                val duration = java.time.Duration.between(shift.start, shift.end).toHours()
                Text(
                    text = "$duration hrs",
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }
    }
}

@Composable
fun CalendarWidget(onDateChange: (year: Int, month: Int, day: Int) -> Unit) {
    val backgroundColor = MaterialTheme.colorScheme.surfaceContainerHigh.toArgb()
    val itemsColor = MaterialTheme.colorScheme.onSurface.toArgb()

    AndroidView(
        factory = { context ->
            CalendarView(context).apply {
                setBackgroundColor(backgroundColor)
                // Note: CalendarView styles are hard to customize programmatically
                // without XML themes, but we can set the background.

                setOnDateChangeListener { _, year, month, day ->
                    // Month is 0-indexed in CalendarView (0 = Jan), so we add 1
                    onDateChange(year, month + 1, day)
                }
            }
        },
        modifier = Modifier.fillMaxWidth()
    )
}