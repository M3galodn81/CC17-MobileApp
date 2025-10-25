package com.example.essence.data.local

import com.example.essence.data.model.BreakdownPAGIBIG
import com.example.essence.data.model.BreakdownPhilHealth
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlin.math.max
import kotlin.math.min

object CalculatorPhilHealth {

    fun computeContribution(grossSalary: Double): BreakdownPhilHealth{
        val contribution = when (grossSalary) {
            in 0.00..10000.00 -> 500.00
            in 10000.01..99999.99 -> grossSalary * 0.05
            else -> 5000.00
        }

        val employeeShare = contribution/2.0
        val employerShare = contribution/2.0

        return BreakdownPhilHealth(
            contribution = contribution,
            employeeShare = employeeShare,
            employerShare = employerShare,
            totalContribution = employeeShare + employerShare
        )
    }
}