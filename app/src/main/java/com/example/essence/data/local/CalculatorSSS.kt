package com.example.essence.data.local

import com.example.essence.data.model.BreakdownSSS
import kotlin.math.floor
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt
import kotlin.math.roundToLong

object CalculatorSSS {
    fun salaryToMsc(grossMonthlySalary: Double): Long {
        val minMsc = 5_000L
        val maxMsc = 35_000L

        // If salary is negative treat as zero
        val salary = max(0.0, grossMonthlySalary)

        // floor to nearest 500: e.g. 5,000..5,499.99 -> 5,000 ; 5,500 -> 5,500
        val floored = floor(salary / 500.0).toLong() * 500L

        // clamp
        return min(max(floored, minMsc), maxMsc)
    }

    fun computeContribution(grossSalary: Double): BreakdownSSS {
        val msc = salaryToMsc(grossSalary).toDouble()

        // Rates
        val employeeRate = 0.05
        val employerRate = 0.10
        val mpf: Double = if (msc < 15000) 10.00 else 30.00


        val employeeShare = ((msc * employeeRate))
        val employerShare = ((msc * employerRate)) + mpf
        val total = employeeShare + employerShare + mpf

        return BreakdownSSS(
            msc = msc,
            employeeShare = employeeShare,
            employerShare = employerShare,
            totalContribution = total
        )
    }
}


