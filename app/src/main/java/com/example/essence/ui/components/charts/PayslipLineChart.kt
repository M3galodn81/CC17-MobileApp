package com.example.essence.ui.components.charts

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.essence.data.model.PayslipData
import java.time.format.DateTimeFormatter
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.log10
import kotlin.math.pow
import kotlin.math.roundToInt

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PayslipLineChart(payslips: List<PayslipData>) {
    val data = payslips.sortedBy { it.payEndDate }

    val netPayValues = data.map {
        it.netPay.toFloat()
    }

    val maxPay = netPayValues.maxOrNull() ?: 0f
    val minPay = netPayValues.minOrNull() ?: 0f
    val lineColor = MaterialTheme.colorScheme.primary

    val textMeasurer = rememberTextMeasurer()
    val axisLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
    val labelStyle = remember {
        TextStyle(
            color = axisLabelColor,
            fontSize = 12.sp
        )
    }

    val monthFormatter = DateTimeFormatter.ofPattern("MMM")
    val xLabels = data.map { it.payStartDate.format(monthFormatter) }

    // --- NEW: Guideline 1, 2, 4 ---
    // Dynamically calculate a "nice" axis scale and step
    val axisDetails = remember(minPay, maxPay) {
        calculateNiceAxis(minPay, maxPay)
    }
    val minAxis = axisDetails.minAxis
    val maxAxis = axisDetails.maxAxis
    val niceStep = axisDetails.step
    // --- End NEW ---

    val axisColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
    val axisStrokeWidth = with(LocalDensity.current) { 1.dp.toPx() }

    // --- NEW: Guideline 5 ---
    // A separate, fainter color for the gridlines
    val gridColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(240.dp)
    ) {
        Text(
            "Net Pay Progress",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp, start = 16.dp)
        )

        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    start = 48.dp,
                    bottom = 40.dp,
                    end = 16.dp,
                    top = 16.dp
                )
        ) {
            if (netPayValues.isEmpty()) return@Canvas

            val height = size.height
            val width = size.width

            // --- MODIFIED: Use the new rounded axis range ---
            val axisValueRange = (maxAxis - minAxis).takeIf { it > 0f } ?: 1f

            val points = netPayValues.mapIndexed { index, value ->
                val x = if (netPayValues.size > 1) {
                    index * (width / (netPayValues.size - 1))
                } else {
                    width / 2f
                }
                // --- MODIFIED: Calculate Y based on new minAxis and axisValueRange ---
                val y = height - ((value - minAxis) / axisValueRange * height)
                Offset(x, y)
            }

            // --- Main Y-axis line (same) ---
            drawLine(
                color = axisColor,
                start = Offset(0f, 0f),
                end = Offset(0f, height),
                strokeWidth = axisStrokeWidth
            )
            // --- Main X-axis line (same) ---
            drawLine(
                color = axisColor,
                start = Offset(0f, height),
                end = Offset(width, height),
                strokeWidth = axisStrokeWidth
            )


            // --- NEW: Y-Axis Labels & Gridlines (Guidelines 2, 3, 5) ---
            // Replaces the old static min/mid/max logic
            val tickValues = (minAxis.toInt()..maxAxis.toInt() step niceStep.toInt())

            tickValues.forEach { tickValue ->
                // Calculate Y position for this tick
                val tickY = height - ((tickValue - minAxis) / axisValueRange * height)

                // Draw Gridline (Guideline 5)
                // (Don't draw for minAxis, as it's the X-axis itself)
                if (tickValue != minAxis.toInt()) {
                    drawLine(
                        color = gridColor,
                        start = Offset(0f, tickY),
                        end = Offset(width, tickY),
                        strokeWidth = axisStrokeWidth // 1.dp
                    )
                }

                // Format and Measure Label (Guideline 3)
                val label = "₱${(tickValue / 1000f).roundToInt()}K"
                val labelResult = textMeasurer.measure(AnnotatedString(label), labelStyle)

                // Draw Label
                drawText(
                    textLayoutResult = labelResult,
                    topLeft = Offset(
                        x = -labelResult.size.width - 4.dp.toPx(),
                        y = tickY - (labelResult.size.height / 2) // Center on the line
                    )
                )
            }
            // --- End NEW ---


            // --- X-axis labels (same as before) ---
            points.forEachIndexed { index, point ->
                val label = xLabels[index]
                val labelResult = textMeasurer.measure(AnnotatedString(label), labelStyle)
                drawText(
                    textLayoutResult = labelResult,
                    topLeft = Offset(
                        x = point.x - (labelResult.size.width / 2),
                        y = height + 4.dp.toPx()
                    )
                )
            }

            // --- Line path (same as before) ---
            if (points.size > 1) {
                val path = Path().apply {
                    moveTo(points.first().x, points.first().y)
                    points.drop(1).forEach {
                        lineTo(it.x, it.y)
                    }
                }
                drawPath(
                    path = path,
                    color = lineColor,
                    style = Stroke(width = 5f)
                )
            }

            // --- Data points (same as before) ---
            points.forEach {
                drawCircle(
                    color = lineColor,
                    radius = 8f,
                    center = it
                )
                drawCircle(
                    color = Color.White,
                    radius = 4f,
                    center = it
                )
            }
        }
    }
}

/**
 * Helper data class to hold the calculated axis properties.
 */
private data class AxisDetails(
    val minAxis: Float,
    val maxAxis: Float,
    val step: Float
)

/**
 * Implements Guidelines 1, 2, and 4.
 * Calculates a "nice" min, max, and step for the Y-axis
 * to ensure rounded, evenly-spaced labels.
 */
private fun calculateNiceAxis(minPay: Float, maxPay: Float): AxisDetails {
    // 1. Calculate the raw data range
    val dataRange = (maxPay - minPay).takeIf { it > 0f } ?: 1000f

    // 2. Aim for 4-6 ticks (Guideline 4)
    val targetTickCount = 5
    val roughStep = dataRange / (targetTickCount - 1)

    // 3. Find the nearest "nice" step (1, 2, 5, 10, 20, 50, 100, 1000...)
    val magnitude = 10f.pow(floor(log10(roughStep)))
    val residual = roughStep / magnitude

    val niceStep = when {
        residual <= 1f -> 1f * magnitude
        residual <= 2f -> 2f * magnitude
        residual <= 5f -> 5f * magnitude
        else -> 10f * magnitude
    }

    // 4. Calculate new min/max based on the nice step (Guideline 1 & 2)
    val minAxis = floor(minPay / niceStep) * niceStep
    val maxAxis = ceil(maxPay / niceStep) * niceStep

    return AxisDetails(minAxis, maxAxis, niceStep)
}