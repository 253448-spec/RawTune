package com.upchiapas.kt_template.rawtune.presentation

import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RawTuneScreen(viewModel: RawTuneViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("RawTune Mobile", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Configurar Hardware",
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedTextField(
                        value = uiState.cpu,
                        onValueChange = {},
                        label = { Text("Procesador (CPU)") },
                        modifier = Modifier.fillMaxWidth(),
                        readOnly = true
                    )

                    Text("Memoria RAM: ${uiState.ram} GB")
                    Slider(
                        value = uiState.ram.toFloat(),
                        onValueChange = { viewModel.actualizarRam(it.toInt()) },
                        valueRange = 4f..64f,
                        steps = 14
                    )

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        FilterChip(
                            selected = uiState.tipoRam == "DDR4",
                            onClick = { viewModel.actualizarRamTipo("DDR4") },
                            label = { Text("DDR4") }
                        )
                        FilterChip(
                            selected = uiState.tipoRam == "DDR5",
                            onClick = { viewModel.actualizarRamTipo("DDR5") },
                            label = { Text("DDR5") }
                        )
                    }
                }
            }

            Button(
                onClick = { viewModel.analizarHardware() },
                modifier = Modifier.fillMaxWidth().height(48.dp),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text("ANALIZAR EQUIPO", fontWeight = FontWeight.Bold)
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = if (uiState.puntuacion >= 70) Color(0xFFE8F5E9) else Color(0xFFFFF3E0)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Puntuación: ${uiState.puntuacion}/100",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Ancho de Banda: ${uiState.anchoBanda} GB/s", fontSize = 14.sp)
                    Text("Cuello de Botella: ${uiState.cuelloBotonella}%", fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(uiState.resumen, fontSize = 13.sp)
                }
            }
        }
    }
}