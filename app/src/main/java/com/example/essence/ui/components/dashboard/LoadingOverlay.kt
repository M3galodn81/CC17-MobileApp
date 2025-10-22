package com.example.essence.ui.components.dashboar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

/**
 * A full-screen overlay with a semi-transparent background and a centered
 * CircularProgressIndicator. It also consumes all clicks, preventing them
 * from reaching the content underneath.
 */
@Composable
fun GlobalLoadingOverlay() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            // Use 'scrim' color for a standard overlay background
            .background(MaterialTheme.colorScheme.scrim.copy(alpha = 0.5f))
            // Consume all clicks
            .clickable(enabled = true, onClick = {}),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}