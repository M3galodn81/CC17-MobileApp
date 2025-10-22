package com.example.essence.functions

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters

// Define your formatters once
@RequiresApi(Build.VERSION_CODES.O)
private val fullMonthFormatter = DateTimeFormatter.ofPattern("MMMM, yyyy")

@RequiresApi(Build.VERSION_CODES.O)
private val partialMonthFormatterStart = DateTimeFormatter.ofPattern("MMM d")

@RequiresApi(Build.VERSION_CODES.O)
private val partialMonthFormatterEnd = DateTimeFormatter.ofPattern("d, yyyy")

/**
 * Formats a date range.
 * If it's a full calendar month, it returns "Month, YYYY".
 * Otherwise, it returns "MMM d - d, YYYY".
 */
@RequiresApi(Build.VERSION_CODES.O)
fun formatPayPeriod(startDate: LocalDate, endDate: LocalDate): String {
    // Check if startDate is the 1st of the month
    val isFirstDay = startDate.dayOfMonth == 1

    // Check if endDate is the last day of the *same* month as startDate
    val isLastDayOfSameMonth = endDate == startDate.with(TemporalAdjusters.lastDayOfMonth())

    return if (isFirstDay && isLastDayOfSameMonth) {
        // It's a full month, use the "Month, YYYY" format
        startDate.format(fullMonthFormatter)
    } else {
        // It's a partial month, use the "MMM d - d, YYYY" format
        val start = startDate.format(partialMonthFormatterStart)
        val end = endDate.format(partialMonthFormatterEnd)
        "$start - $end"
    }
}