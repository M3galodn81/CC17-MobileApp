package com.example.essence.data.local

import com.example.essence.data.model.BreakdownPAGIBIG
import com.example.essence.data.model.BreakdownSSS
import kotlin.Double
import kotlin.math.max
import kotlin.math.min

object CalculatorPAGIBIG {

    fun computeContribution(grossSalary: Double): BreakdownPAGIBIG{
        val msc = grossSalary

        // Rates
        val employeeRate = if (msc > 1500) 0.02 else 0.01
        val employerRate = 0.02

        val employeeShareCents = ((msc * employeeRate))
        val employerShareCents = ((msc * employerRate))
        val total = employeeShareCents + employerShareCents

        return BreakdownPAGIBIG(
            employeeRate = employeeRate ,
            employeeShare = employeeShareCents,
            employerShare = employerShareCents,
            totalContribution = total
        )
    }
}