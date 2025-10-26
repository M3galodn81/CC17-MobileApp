package com.example.essence.data.model

import java.time.LocalDateTime

// Data class to represent a single shift
data class Shift(
    val id: String,
    val employeeId: String,
    val start: LocalDateTime,
    val end: LocalDateTime,
    val role: String? = null // e.g., "Cashier", "Manager"
)

