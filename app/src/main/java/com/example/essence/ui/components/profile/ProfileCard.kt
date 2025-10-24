package com.example.essence.ui.components.profile

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.essence.data.model.User
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ProfileCard(
    user: User
) {
    // Formatter for displaying dates
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
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // --- Section 1: Main Identity ---
            Text(
                text = user.fullName,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = user.position,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(Modifier.height(16.dp))
            Divider()
            Spacer(Modifier.height(16.dp))

            // --- Section 2: Employment Details ---
            Text(
                text = "Employment Details",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            ProfileInfoRow(label = "Employee ID", value = user.id)
            ProfileInfoRow(label = "Department", value = user.department)
            ProfileInfoRow(label = "Hire Date", value = user.hireDate.format(dateFormat))

            Spacer(Modifier.height(16.dp))
            Divider()
            Spacer(Modifier.height(16.dp))

            // --- Section 3: Contact & Personal ---
            Text(
                text = "Contact & Personal",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            ProfileInfoRow(label = "Email", value = user.email)
            ProfileInfoRow(label = "Phone", value = user.phone ?: "N/A")
            ProfileInfoRow(label = "Address", value = user.address ?: "N/A")
            ProfileInfoRow(
                label = "Birthday",
                value = user.birthDate?.format(dateFormat) ?: "N/A"
            )

            Spacer(Modifier.height(16.dp))
            Divider()
            Spacer(Modifier.height(16.dp))

            // --- Section 4: Government IDs ---
            Text(
                text = "Government IDs",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            ProfileInfoRow(label = "SSS Number", value = user.sssNumber ?: "N/A")
            ProfileInfoRow(label = "TIN", value = user.tin ?: "N/A")
            ProfileInfoRow(label = "PhilHealth", value = user.philHealthNumber ?: "N/A")
            ProfileInfoRow(label = "Pag-Ibig", value = user.pagIbigNumber ?: "N/A")
        }
    }
}

/**
 * A helper composable to create a labeled row of information.
 * It neatly aligns a label and a value.
 */
@Composable
private fun ProfileInfoRow(
    label: String,
    value: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.Top // Use Top in case 'value' wraps to multiple lines
    ) {
        // Label
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.weight(0.4f) // Label takes 40% of the width
        )
        // Value
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.weight(0.6f) // Value takes 60% of the width
        )
    }
}