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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.essence.data.local.SessionManager
import com.example.essence.data.model.PayslipData
import com.example.essence.data.sample.SampleData
import com.example.essence.ui.components.payslip.PayslipCard
import com.example.essence.data.sample.payslips
import com.example.essence.functions.formatPayPeriod
import com.example.essence.ui.components.payslip.Payslip13thMonthCard
import com.example.essence.ui.components.payslip.PayslipLineChart


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PayslipContent(onPayslipSelected: (PayslipData) -> Unit) {

    Column(
        modifier = screenModifier(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        val context = LocalContext.current
        var UserPayslips = SampleData.userMapById[SessionManager.getUserId(context)]?.payslips as List<PayslipData>
        PayslipLineChart(UserPayslips)
        Payslip13thMonthCard(UserPayslips)
        UserPayslips.forEach { payslip ->
            PayslipCard(
                payPeriod = formatPayPeriod(payslip.payStartDate,payslip.payEndDate),
                grossPay = "₱${"%,.2f".format(payslip.totalEarnings)}",
                deductions = "₱${"%,.2f".format(payslip.totalDeductions)}",
                netPay = "₱${"%,.2f".format(payslip.netPay)}",
                onViewDetails = { onPayslipSelected(payslip)
                }
            )
        }



    }
}


