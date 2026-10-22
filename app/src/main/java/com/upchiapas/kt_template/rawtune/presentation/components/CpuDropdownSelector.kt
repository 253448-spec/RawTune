package com.upchiapas.kt_template.rawtune.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.upchiapas.kt_template.rawtune.data.model.CpuOption

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CpuDropdownSelector(
    availableCpus: List<CpuOption>,
    selectedCpu: CpuOption?,
    onCpuSelected: (CpuOption) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = selectedCpu?.name ?: "Seleccione un procesador",
            onValueChange = {},
            readOnly = true,
            label = { Text("Procesador (CPU)") },
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
                        Text(
                            text = "${cpu.name} (${cpu.cores}C/${cpu.threads}T - Recomendado ${cpu.recommendedRamType})",
                            fontWeight = if (cpu.id == selectedCpu?.id) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    onClick = {
                        onCpuSelected(cpu)
                        expanded = false
                    }
                )
            }
        }
    }
}
