package com.example.essence.data.model

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
    val tax: Double,
    val sss: Double,
    val philHealth: Double,
    val pagIbig: Double,
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
        get() = tax + sss + philHealth + pagIbig + otherDeductions

    val netPay: Double
        get() = totalEarnings - totalDeductions
}