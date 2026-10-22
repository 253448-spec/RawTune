package com.upchiapas.kt_template.rawtune.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.upchiapas.kt_template.rawtune.data.model.CpuOption

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CpuDropdownSelector(
    availableCpus: List<CpuOption>,
    selectedCpu: CpuOption?,
    onCpuSelected: (CpuOption) -> Unit,
    onOpenAddCpuDialog: () -> Unit,
    onDeleteCustomCpu: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Procesador (CPU)",
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp
            )
            FilledTonalButton(
                onClick = onOpenAddCpuDialog,
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("+ AGREGAR CPU", fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = selectedCpu?.name ?: "Seleccione un procesador",
                onValueChange = {},
                readOnly = true,
                label = { Text("Modelo de Procesador") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
                modifier = Modifier
                    .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
                    .fillMaxWidth()
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                availableCpus.forEach { cpu ->
                    DropdownMenuItem(
                        text = {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = cpu.name,
                                        fontWeight = if (cpu.id == selectedCpu?.id) FontWeight.Bold else FontWeight.Normal
                                    )
                                    Text(
                                        text = "${cpu.cores}C/${cpu.threads}T • ${cpu.recommendedRamType} • ${if (cpu.hasIntegratedGpu) "GPU Integrado" else "Gráfica Dedicada"}",
                                        fontSize = 11.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                if (cpu.id.startsWith("custom_")) {
                                    IconButton(
                                        onClick = { onDeleteCustomCpu(cpu.id) },
                                        modifier = Modifier.size(28.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Delete,
                                            contentDescription = "Eliminar CPU",
                                            tint = MaterialTheme.colorScheme.error,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }
                            }
                        },
                        onClick = {
                            onCpuSelected(cpu)
                            expanded = false
                        }
                    )
                }
            }
        }

        // Ficha técnica del procesador seleccionado (Recomendaciones y alertas)
        selectedCpu?.let { cpu ->
            OutlinedCard(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.outlinedCardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f)
                )
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Badges técnicos
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        SuggestionChip(
                            onClick = {},
                            label = { Text("${cpu.cores}C / ${cpu.threads}T", fontSize = 11.sp) }
                        )
                        SuggestionChip(
                            onClick = {},
                            label = {
                                Text(
                                    if (cpu.hasIntegratedGpu) "🎮 ${cpu.integratedGpuName}" else "🖥️ Gráfica Dedicada",
                                    fontSize = 11.sp
                                )
                            }
                        )
                        SuggestionChip(
                            onClick = {},
                            label = { Text("RAM ${cpu.recommendedRamType}", fontSize = 11.sp) }
                        )
                    }

                    // Propósito / Descripción
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(16.dp).padding(top = 2.dp)
                        )
                        Text(
                            text = cpu.description,
                            fontSize = 12.sp,
                            lineHeight = 16.sp
                        )
                    }

                    // Alerta específica de RAM
                    if (cpu.ramRecommendation.isNotBlank()) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Icon(
                                imageVector = Icons.Default.Warning,
                                contentDescription = null,
                                tint = Color(0xFFF57C00),
                                modifier = Modifier.size(16.dp).padding(top = 2.dp)
                            )
                            Text(
                                text = cpu.ramRecommendation,
                                fontSize = 12.sp,
                                lineHeight = 16.sp,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }
        }
    }
}
