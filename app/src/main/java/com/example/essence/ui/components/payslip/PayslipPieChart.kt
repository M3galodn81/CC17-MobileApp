package com.example.essence.ui.components.payslip

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
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
        "Tax" to payslip.tax,
        "SSS" to payslip.sss,
        "PhilHealth" to payslip.philHealth,
        "Pag-IBIG" to payslip.pagIbig,
        "Others" to payslip.otherDeductions
    )

    val total = deductions.sumOf { it.second }

    // Color palette (consistent with Material theme)
    val colors = listOf(
        Color(0xFFE57373), // Tax - red
        Color(0xFF64B5F6), // SSS - blue
        Color(0xFF81C784), // PhilHealth - green
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

        // Legend
        deductions.forEachIndexed { index, (label, value) ->
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
