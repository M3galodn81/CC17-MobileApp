package com.example.essence.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.essence.ui.components.PayslipCard
import com.example.essence.data.sample.payslips
import com.example.essence.ui.components.charts.PayslipLineChart


@Composable
fun PayslipContent() {
    // Sample monthly payslip data


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        PayslipLineChart(payslips)

        payslips.forEach { payslip ->
            PayslipCard(
                payPeriod = "${payslip.payStartDate} - ${payslip.payEndDate}",
                grossPay = "₱${"%,.2f".format(payslip.totalEarnings)}",
                deductions = "₱${"%,.2f".format(payslip.totalDeductions)}",
                netPay = "₱${"%,.2f".format(payslip.netPay)}"
            )
        }

    }
}


