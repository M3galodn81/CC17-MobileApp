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
import com.example.essence.ui.components.PayslipCard
import com.example.essence.data.sample.payslips
import com.example.essence.functions.formatPayPeriod
import com.example.essence.ui.components.charts.PayslipLineChart
import com.example.essence.ui.components.charts.PayslipPieChart


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PayslipDetailsScreen(
    payslipData: PayslipData
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        PayslipPieChart(payslipData)

    }
}


