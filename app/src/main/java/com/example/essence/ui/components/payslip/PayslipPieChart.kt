package com.example.essence.ui.components.payslip

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.essence.data.model.PayslipData
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun GrossPayChart(
    payslip: PayslipData,
    modifier: Modifier = Modifier
) {
    val textMeasurer = rememberTextMeasurer()
    val labelStyle = TextStyle(color = Color.White, fontSize = 12.sp)

    // Extract deductions and group them
    val earnings = listOf(
        "Basic Pay" to payslip.basicPay,
        "Overtime Pay" to payslip.overtimePay,
        "Holiday Pay" to payslip.holidayPay,
        "Allowances" to payslip.allowances,
        "Bonuses" to payslip.bonuses
    )

    val total = earnings.sumOf { it.second }

    // Color palette (consistent with Material theme)
    val colors = listOf(
        Color(0xFF4CAF50), // Basic Pay - green (steady income)
        Color(0xFF64B5F6), // Overtime Pay - blue (extra work)
        Color(0xFFFFD54F), // Holiday Pay - gold (special)
        Color(0xFFFF8A65), // Allowances - orange (benefits)
        Color(0xFFBA68C8)  // Bonuses - purple (reward)
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            "Gross Pay Breakdown",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Box(
            modifier = Modifier
                .height(240.dp)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                if (total == 0.0) return@Canvas

                val radius = size.minDimension / 2.2f
                var startAngle = -90f // start at top

                earnings.forEachIndexed { index, (label, value) ->
                    val sweepAngle = (value / total * 360f).toFloat()
                    val color = colors[index % colors.size]

                    // Draw slice
                    drawArc(
                        color = color,
                        startAngle = startAngle,
                        sweepAngle = sweepAngle,
                        useCenter = true,
                        size = Size(radius * 2, radius * 2),
                        topLeft = Offset(
                            (size.width - radius * 2) / 2,
                            (size.height - radius * 2) / 2
                        )
                    )

                    // Label position
                    val middleAngle = Math.toRadians((startAngle + sweepAngle / 2).toDouble())
                    val labelX = size.width / 2 + cos(middleAngle) * radius * 0.6
                    val labelY = size.height / 2 + sin(middleAngle) * radius * 0.6

                    if (value > 0) {
                        val percent = (value / total * 100).toInt()
                        val textLayout = textMeasurer.measure(
                            AnnotatedString("$percent%"),
                            style = labelStyle
                        )
                        drawText(
                            textLayoutResult = textLayout,
                            topLeft = Offset(
                                (labelX - textLayout.size.width / 2).toFloat(),
                                (labelY + textLayout.size.height / 4).toFloat()
                            )
                        )

                    }

                    startAngle += sweepAngle
                }
            }

            // Center text
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Total", style = MaterialTheme.typography.bodySmall)
                Text(
                    "₱${"%,.2f".format(total)}",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }

        Spacer(Modifier.height(16.dp))

        // Legend
        earnings.forEachIndexed { index, (label, value) ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 2.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row {
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .padding(end = 8.dp)
                            .then(Modifier.background(colors[index]))
                    )
                    Text(label)
                }
                Text("₱${"%,.2f".format(value)}")
            }
        }
    }
}

@Composable
fun DeductionsChart(
    payslip: PayslipData,
    modifier: Modifier = Modifier
) {
    val textMeasurer = rememberTextMeasurer()
    val labelStyle = TextStyle(color = Color.White, fontSize = 12.sp)

    // Extract deductions and group them
    val deductions = listOf(
        "SSS" to payslip.sss.employeeShare,
        "PhilHealth" to payslip.philHealth.employeeShare,
        "Pag-IBIG" to payslip.pagIbig.employeeShare,
        "Others" to payslip.otherDeductions
    )
    val total = deductions.sumOf { it.second }

    // Color palette (consistent with Material theme)
    val colors = listOf(
        Color(0xFF64B5F6), // SSS - blue
        Color(0xFF81C784), // PhilHealth - green
        Color(0xFFE57373), // Tax - red
        Color(0xFFFFB74D), // Pag-IBIG - orange
        Color(0xFFBA68C8)  // Other - purple
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            "Deductions Breakdown",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Box(
            modifier = Modifier
                .height(240.dp)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                if (total == 0.0) return@Canvas

                val radius = size.minDimension / 2.2f
                var startAngle = -90f // start at top

                deductions.forEachIndexed { index, (label, value) ->
                    val sweepAngle = (value / total * 360f).toFloat()
                    val color = colors[index % colors.size]

                    // Draw slice
                    drawArc(
                        color = color,
                        startAngle = startAngle,
                        sweepAngle = sweepAngle,
                        useCenter = true,
                        size = Size(radius * 2, radius * 2),
                        topLeft = Offset(
                            (size.width - radius * 2) / 2,
                            (size.height - radius * 2) / 2
                        )
                    )

                    // Label position
                    val middleAngle = Math.toRadians((startAngle + sweepAngle / 2).toDouble())
                    val labelX = size.width / 2 + cos(middleAngle) * radius * 0.6
                    val labelY = size.height / 2 + sin(middleAngle) * radius * 0.6

                    if (value > 0) {
                        val percent = (value / total * 100).toInt()
                        val textLayout = textMeasurer.measure(
                            AnnotatedString("$percent%"),
                            style = labelStyle
                        )
                        drawText(
                            textLayoutResult = textLayout,
                            topLeft = Offset(
                                (labelX - textLayout.size.width / 2).toFloat(),
                                (labelY + textLayout.size.height / 4).toFloat()
                            )
                        )

                    }

                    startAngle += sweepAngle
                }
            }

            // Center text
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Total", style = MaterialTheme.typography.bodySmall)
                Text(
                    "₱${"%,.2f".format(total)}",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }

        Spacer(Modifier.height(16.dp))

        // =================================================
        // START: Updated & Simplified Legend
        // =================================================
        Column(modifier = Modifier.fillMaxWidth()) {

            // --- SSS ---
            val sss = payslip.sss
            val sssDetails = if (sss.msc > 0) {
                "5.0% of MSC (₱${"%,.0f".format(sss.msc)})"
            } else null

            DeductionLegendItem(
                label = "SSS",
                color = colors[0],
                employeeShare = sss.employeeShare,
                details = sssDetails
            )

            // --- PhilHealth ---
            val philhealth = payslip.philHealth
            val phDetails = when (philhealth.employeeShare) {
                // Min cap
                250.0 -> "Fixed Premium (Basis <= ₱10,000)"
                // Max cap
                2500.0 -> "Fixed Premium (Basis >= ₱100,000)"
                // Variable rate
                else -> {
                    val grossSalaryBasis = philhealth.employeeShare / 0.025
                    "2.5% of (₱${"%,.2f".format(grossSalaryBasis)})"
                }
            }

            DeductionLegendItem(
                label = "PhilHealth",
                color = colors[1],
                employeeShare = philhealth.employeeShare,
                details = phDetails
            )

            // --- Pag-IBIG ---
            val pagibig = payslip.pagIbig
            val pagibigDetails = if (pagibig.employeeRate > 0) {
                "${"%.0f".format(pagibig.employeeRate * 100)}% Contribution"
            } else null

            DeductionLegendItem(
                label = "Pag-IBIG",
                color = colors[2],
                employeeShare = pagibig.employeeShare,
                details = pagibigDetails
            )

            // --- Others ---
            DeductionLegendItem(
                label = "Others",
                color = colors[3],
                employeeShare = payslip.otherDeductions,
                details = null // No details for "Others"
            )


        }
    }
}


@Composable
private fun DeductionLegendItem(
    label: String,
    color: Color,
    employeeShare: Double,
    details: String? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = CenterVertically
    ) {
        // Color box and Main Label
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .background(color)
            )
            Spacer(Modifier.width(8.dp))
            Text(label, style = MaterialTheme.typography.bodyMedium)
        }

        // Breakdown Text
        Column(horizontalAlignment = Alignment.End) {
            // Employee Share (the amount deducted)
            Text(
                "₱${"%,.2f".format(employeeShare)}",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold
            )
            // Show the new details string if it exists
            if (details != null) {
                Text(
                    text = details,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}