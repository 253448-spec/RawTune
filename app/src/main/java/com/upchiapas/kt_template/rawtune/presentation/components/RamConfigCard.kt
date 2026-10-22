package com.upchiapas.kt_template.rawtune.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun RamConfigCard(
    ramGb: Int,
    ramType: String,
    onRamChanged: (Int) -> Unit,
    onRamTypeChanged: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Capacidad de RAM",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp
                )
                Badge(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ) {
                    Text("$ramGb GB", fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp))
                }
            }

            Slider(
                value = ramGb.toFloat(),
                onValueChange = { onRamChanged(it.toInt()) },
                valueRange = 4f..64f,
                steps = 14
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("4 GB", fontSize = 12.sp, color = MaterialTheme.colorScheme.outline)
                Text("16 GB", fontSize = 12.sp, color = MaterialTheme.colorScheme.outline)
                Text("32 GB", fontSize = 12.sp, color = MaterialTheme.colorScheme.outline)
                Text("64 GB", fontSize = 12.sp, color = MaterialTheme.colorScheme.outline)
            }

            HorizontalDivider()

            Text(
                text = "Tecnología de Memoria",
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp
            )

            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                FilterChip(
                    selected = ramType == "DDR4",
                    onClick = { onRamTypeChanged("DDR4") },
                    label = { Text("DDR4 (3200 MT/s)") }
                )
                FilterChip(
                    selected = ramType == "DDR5",
                    onClick = { onRamTypeChanged("DDR5") },
                    label = { Text("DDR5 (6000 MT/s)") }
                )
            }
        }
    }
}
