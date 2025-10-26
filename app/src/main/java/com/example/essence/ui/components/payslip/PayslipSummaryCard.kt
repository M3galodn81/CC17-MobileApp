package com.example.essence.ui.components.payslip

import android.R
import android.os.Build
import android.widget.Space
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.essence.data.model.PayslipData
import java.text.NumberFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import com.example.essence.data.sample.payslips

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PayslipSummaryCard(
    payslipData: PayslipData,
){
    val dateFormat = DateTimeFormatter.ofPattern("MMMM dd, yyyy")
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(8.dp)
    ){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Summary",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(4.dp))
            // Top: Pay Start Date
            Row(){
                Text(
                    text = "Start Date",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Left,
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = (payslipData.payStartDate).format(dateFormat),
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Right,
                )
            }

            Row(){
                Text(
                    text = "End Date",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Left,
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = (payslipData.payEndDate).format(dateFormat),
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Right,
                )
            }

            Row(){
                Text(
                    text = "Pay Date",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Left,
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = (payslipData.payDate).format(dateFormat),
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Right,
                )
            }

            Spacer(modifier = Modifier.height(4.dp))


            if (payslipData.remarks != null){
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Remarks",
                    style = MaterialTheme.typography.titleLarge,
                )
                Text(text = payslipData.remarks)
            }
        }
    }
}


/**
 * A new card that focuses on the financial breakdown:
 * Total Earnings, Total Deductions, and Net Pay.
 */
@Composable
fun PayslipFinancialSummaryCard(
    payslipData: PayslipData,
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp) // Adds space between rows
        ) {
            // Card Title
            Text(
                text = "Pay Summary",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Total Earnings
            SummaryRow(
                label = "Total Earnings",
                value = payslipData.totalEarnings.toCurrency()
            )

            // Total Deductions
            SummaryRow(
                label = "Mandatory Deductions",
                value = payslipData.mandatoryDeductions.toCurrency()
            )

            SummaryRow(
                label = "Other Deductions",
                value = payslipData.otherDeductions.toCurrency()
            )

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            // Net Pay (Highlighted)
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Net Pay",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = payslipData.netPay.toCurrency(),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary // Highlight color
                )
            }
            val totalContribution = (payslipData.totalDeductions + payslipData.otherDeductions)
            val afterContributionPercentage = (payslipData.totalEarnings - totalContribution)/ payslipData.totalEarnings

            Row(){

                Text(
                    text = "Net Salary Percentage",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Left,
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "${"%.2f".format(afterContributionPercentage * 100)}%",
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Right,
                )
            }
        }
    }
}

/**
 * A helper composable to create a standard label/value row.
 */
@Composable
private fun SummaryRow(
    label: String,
    value: String,
    isDeduction: Boolean = false
) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = if (isDeduction) "(${value})" else value,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

private fun Double.toCurrency(): String {
    val format = NumberFormat.getCurrencyInstance(Locale("en", "PH"))
    return format.format(this)
}