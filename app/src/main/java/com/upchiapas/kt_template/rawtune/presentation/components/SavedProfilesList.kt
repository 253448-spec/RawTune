package com.upchiapas.kt_template.rawtune.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.upchiapas.kt_template.rawtune.data.model.SavedHardwareProfile

@Composable
fun SavedProfilesList(
    profiles: List<SavedHardwareProfile>,
    onLoadProfile: (SavedHardwareProfile) -> Unit,
    onDeleteProfile: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = "Historial Persistente (${profiles.size} Ensambles)",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )

            if (profiles.isEmpty()) {
                Text(
                    text = "No hay perfiles guardados. Configura uno y presiona 'Guardar'.",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.outline
                )
            } else {
                profiles.forEach { profile ->
                    OutlinedCard(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = profile.name,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                                Text(
                                    text = "${profile.cpuName} | ${profile.ramGb}GB ${profile.ramType}",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = "Puntaje: ${profile.score}/100 | Cuello: ${profile.bottleneck}%",
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }

                            Row {
                                IconButton(onClick = { onLoadProfile(profile) }) {
                                    Icon(
                                        imageVector = Icons.Default.FileDownload,
                                        contentDescription = "Cargar Perfil",
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }
                                IconButton(onClick = { onDeleteProfile(profile.id) }) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Eliminar",
                                        tint = MaterialTheme.colorScheme.error
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
