package com.upchiapas.kt_template.rawtune.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DeveloperBoard
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties

@Composable
fun AddCpuDialog(
    show: Boolean,
    name: String,
    cores: Int,
    threads: Int,
    score: Int,
    ramType: String,
    hasGpu: Boolean,
    gpuName: String,
    description: String,
    ramAlert: String,
    onNameChange: (String) -> Unit,
    onCoresChange: (Int) -> Unit,
    onThreadsChange: (Int) -> Unit,
    onScoreChange: (Int) -> Unit,
    onRamTypeChange: (String) -> Unit,
    onHasGpuChange: (Boolean) -> Unit,
    onGpuNameChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onRamAlertChange: (String) -> Unit,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    if (!show) return

    AlertDialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false),
        modifier = Modifier
            .fillMaxWidth(0.95f)
            .padding(16.dp),
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.DeveloperBoard,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Añadir Nuevo Procesador",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Ingresa las especificaciones del nuevo procesador para calcular sus diagnósticos y persistirlo en la app.",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                // Nombre
                OutlinedTextField(
                    value = name,
                    onValueChange = onNameChange,
                    label = { Text("Nombre del Procesador") },
                    placeholder = { Text("Ej: Intel Core i9-14900K") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                // Núcleos e Hilos
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Núcleos: $cores", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                        Slider(
                            value = cores.toFloat(),
                            onValueChange = { onCoresChange(it.toInt()) },
                            valueRange = 2f..32f,
                            steps = 14
                        )
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Hilos: $threads", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                        Slider(
                            value = threads.toFloat(),
                            onValueChange = { onThreadsChange(it.toInt()) },
                            valueRange = 2f..64f,
                            steps = 30
                        )
                    }
                }

                // Puntaje Base
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Puntaje Base de Rendimiento", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                        Text("$score / 100", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                    }
                    Slider(
                        value = score.toFloat(),
                        onValueChange = { onScoreChange(it.toInt()) },
                        valueRange = 30f..100f
                    )
                }

                // RAM Recomendada
                Text("Tipo de RAM Recomendada", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    FilterChip(
                        selected = ramType == "DDR4",
                        onClick = { onRamTypeChange("DDR4") },
                        label = { Text("DDR4") }
                    )
                    FilterChip(
                        selected = ramType == "DDR5",
                        onClick = { onRamTypeChange("DDR5") },
                        label = { Text("DDR5") }
                    )
                }

                HorizontalDivider()

                // ¿Tiene GPU integrado?
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("¿Tiene Gráficos Integrados?", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                        Text("Comparte memoria RAM con el sistema", fontSize = 11.sp, color = MaterialTheme.colorScheme.outline)
                    }
                    Switch(
                        checked = hasGpu,
                        onCheckedChange = onHasGpuChange
                    )
                }

                if (hasGpu) {
                    OutlinedTextField(
                        value = gpuName,
                        onValueChange = onGpuNameChange,
                        label = { Text("Nombre del GPU Integrado") },
                        placeholder = { Text("Ej: Radeon Vega 8 o Intel UHD 770") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                // Descripción / Para qué sirve
                OutlinedTextField(
                    value = description,
                    onValueChange = onDescriptionChange,
                    label = { Text("¿Para qué sirve? (Descripción / Propósito)") },
                    placeholder = { Text("Ej: Ideal para gaming competitivo 1440p y edición pesada...") },
                    maxLines = 3,
                    modifier = Modifier.fillMaxWidth()
                )

                // Alerta o Recomendación de RAM
                OutlinedTextField(
                    value = ramAlert,
                    onValueChange = onRamAlertChange,
                    label = { Text("Alerta o Consejo de Memoria RAM") },
                    placeholder = { Text("Ej: Requiere Dual Channel a 3600MHz para no perder rendimiento...") },
                    maxLines = 3,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(onClick = onConfirm) {
                Icon(Icons.Default.Add, contentDescription = null)
                Spacer(modifier = Modifier.width(4.dp))
                Text("GUARDAR PROCESADOR")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}
