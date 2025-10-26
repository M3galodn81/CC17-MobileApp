package com.example.essence.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.essence.data.model.PayslipData
import com.example.essence.ui.components.payslip.DeductionsChart
import com.example.essence.ui.components.payslip.GrossPayChart
import com.example.essence.ui.components.payslip.PayslipFinancialSummaryCard
import com.example.essence.ui.components.payslip.PayslipSummaryCard


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PayslipDetailsScreen(
    payslipData: PayslipData
) {
    Column(
        modifier = screenModifier(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        PayslipSummaryCard(payslipData)
        PayslipFinancialSummaryCard(payslipData)
        GrossPayChart(payslipData)
        DeductionsChart(payslipData)

    }
}


