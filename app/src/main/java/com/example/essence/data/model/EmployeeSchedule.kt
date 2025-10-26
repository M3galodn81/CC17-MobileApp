package com.example.essence.data.model

// Data class for an employee and their list of shifts for the view
data class EmployeeSchedule(
    val employeeId: String,
    val employeeName: String,
    val shifts: List<Shift>
)
