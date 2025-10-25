package com.example.essence.data.model

data class BreakdownSSS(
    val msc: Double,               // monthly salary credit in PHP (whole pesos)
    val employeeShare: Double,     // in centavos (PHP * 100)
    val employerShare: Double,     // in centavos
    val totalContribution: Double  // in centavos
)
