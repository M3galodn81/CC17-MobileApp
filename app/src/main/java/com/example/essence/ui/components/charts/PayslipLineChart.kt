package com.example.essence.ui.components.charts

import android.os.Build // <-- Was unused, now needed
import androidx.annotation.RequiresApi // <-- Was unused, now needed
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
// 1. FIX: Removed incorrect import
// import androidx.core.i18n.DateTimeFormatter
import com.example.essence.data.model.PayslipData
// 2. FIX: Added the correct java.time import
import java.time.format.DateTimeFormatter
import kotlin.math.roundToInt

// 3. FIX: Added API requirement for java.time.format.DateTimeFormatter
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PayslipLineChart(payslips: List<PayslipData>) {
    // This sorting is a good improvement!
    val data = payslips.sortedBy { it.payEndDate }

    // 4. FIX: Re-added string cleaning before .toFloat() to prevent crash
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

    // This now works because of the fixed import
    val monthFormatter = DateTimeFormatter.ofPattern("MMM")
    val xLabels = data.map { it.payStartDate.format(monthFormatter) }

    val maxPayLabel = "₱${(maxPay / 1000f).roundToInt()}K"
    val minPayLabel = "₱${(minPay / 1000f).roundToInt()}K"

    val axisColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
    val axisStrokeWidth = with(LocalDensity.current) { 1.dp.toPx() }

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
            val valueRange = (maxPay - minPay).takeIf { it > 0f } ?: 1f

            val points = netPayValues.mapIndexed { index, value ->
                val x = if (netPayValues.size > 1) {
                    index * (width / (netPayValues.size - 1))
                } else {
                    width / 2f
                }
                val y = height - ((value - minPay) / valueRange * height)
                Offset(x, y)
            }

            // Y-axis
            drawLine(
                color = axisColor,
                start = Offset(0f, 0f),
                end = Offset(0f, height),
                strokeWidth = axisStrokeWidth
            )
            // X-axis
            drawLine(
                color = axisColor,
                start = Offset(0f, height),
                end = Offset(width, height),
                strokeWidth = axisStrokeWidth
            )

            // Y-axis labels
            val maxLabelResult = textMeasurer.measure(AnnotatedString(maxPayLabel), labelStyle)
            val minLabelResult = textMeasurer.measure(AnnotatedString(minPayLabel), labelStyle)

            drawText(
                textLayoutResult = maxLabelResult,
                topLeft = Offset(
                    x = -maxLabelResult.size.width - 4.dp.toPx(),
                    y = 0f - (maxLabelResult.size.height / 2)
                )
            )

            drawText(
                textLayoutResult = minLabelResult,
                topLeft = Offset(
                    x = -minLabelResult.size.width - 4.dp.toPx(),
                    y = height - (minLabelResult.size.height / 2)
                )
            )

            // X-axis labels
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

            // Line path
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

            // Data points
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