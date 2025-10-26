package com.example.essence.data.model

import com.example.essence.data.local.CalculatorPAGIBIG
import com.example.essence.data.local.CalculatorPhilHealth
import com.example.essence.data.local.CalculatorSSS
import java.time.LocalDate

data class PayslipData(
    val id: String,
    val payStartDate: LocalDate,
    val payEndDate: LocalDate,
    val payDate: LocalDate,
    val basicPay: Double,
    val overtimePay: Double,
    val holidayPay: Double,
    val allowances: Double,
    val bonuses: Double,


    val otherDeductions: Double,

    val remarks: String? = null,
    val employeeId: String? = null,
    val employeeName: String? = null,
    val department: String? = null,
    val position: String? = null
) {
    val totalEarnings: Double
        get() = basicPay + overtimePay + holidayPay + allowances + bonuses

    val totalDeductions: Double
        get() = sss.employeeShare +
                philHealth.employeeShare +
                pagIbig.employeeShare + otherDeductions

    val mandatoryDeductions: Double
        get() = sss.employeeShare +
                philHealth.employeeShare +
                pagIbig.employeeShare

    val netPay: Double
        get() = totalEarnings - totalDeductions

    val sss: BreakdownSSS
        get() = CalculatorSSS.computeContribution(totalEarnings)

    val philHealth: BreakdownPhilHealth
        get() = CalculatorPhilHealth.computeContribution(totalEarnings)
    val pagIbig: BreakdownPAGIBIG
        get() = CalculatorPAGIBIG.computeContribution(totalEarnings)
}