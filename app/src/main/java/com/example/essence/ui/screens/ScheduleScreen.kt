package com.example.essence.ui.screens

import android.widget.CalendarView
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView


@Composable
fun ScheduleContent() {
    var selectedDate by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())

    ) {
        CalendarWidget{
                year, month, day ->
            selectedDate = "$year-${month.toString().padStart(2, '0')}-${day.toString().padStart(2, '0')}"
        }
        repeat(50) {
            Text("Schedule entry #$it", color = MaterialTheme.colorScheme.onBackground)
        }
    }
}



@Composable
fun CalendarWidget(onDateChange: (year: Int, month: Int, day: Int) -> Unit) {
    val backgroundColor = MaterialTheme.colorScheme.secondary

    AndroidView(
        factory = { context ->
            CalendarView(context).apply {
                setBackgroundColor(backgroundColor.toArgb())
                setOnDateChangeListener { _, year, month, day ->
                    onDateChange(year, month + 1, day)
                }
            }
        },
        modifier = Modifier.fillMaxWidth()
    )
}