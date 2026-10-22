package com.upchiapas.kt_template.rawtune.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.upchiapas.kt_template.KtApplication
import com.upchiapas.kt_template.rawtune.data.model.AnalysisResult
import com.upchiapas.kt_template.rawtune.data.model.CpuOption
import com.upchiapas.kt_template.rawtune.data.model.SavedHardwareProfile
import com.upchiapas.kt_template.rawtune.presentation.components.*
import com.upchiapas.kt_template.rawtune.presentation.viewmodels.RawTuneViewModel

/**
 * Stateful Composable: Se encarga de conectar el ViewModel con el ciclo de vida
 * y delegar el renderizado al Composable Stateless [RawTuneContent].
 */
@Composable
fun RawTunePage(
    viewModel: RawTuneViewModel = viewModel(
        factory = (LocalContext.current.applicationContext as KtApplication).rawTuneModule.factoryRawTune
    )
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    RawTuneContent(
        uiState = uiState,
        onCpuSelected = { cpu -> viewModel.selectCpu(cpu) },
        onRamChanged = { ram -> viewModel.updateRam(ram) },
        onRamTypeChanged = { type -> viewModel.updateRamType(type) },
        onProfileNameChanged = { name -> viewModel.updateProfileName(name) },
        onAnalyze = { viewModel.analyzeHardware() },
        onSaveProfile = { viewModel.saveCurrentProfile() },
        onDeleteProfile = { id -> viewModel.deleteProfile(id) },
        onLoadProfile = { profile -> viewModel.loadProfile(profile) },
        onDismissMessage = { viewModel.clearUserMessage() },

        // Eventos para el nuevo procesador manual
        onOpenAddCpuDialog = { viewModel.toggleAddCpuDialog(true) },
        onCloseAddCpuDialog = { viewModel.toggleAddCpuDialog(false) },
        onDeleteCustomCpu = { id -> viewModel.deleteCustomCpu(id) },
        onNewCpuNameChange = { name -> viewModel.updateNewCpuName(name) },
        onNewCpuCoresChange = { cores -> viewModel.updateNewCpuCores(cores) },
        onNewCpuThreadsChange = { threads -> viewModel.updateNewCpuThreads(threads) },
        onNewCpuScoreChange = { score -> viewModel.updateNewCpuScore(score) },
        onNewCpuRamTypeChange = { type -> viewModel.updateNewCpuRamType(type) },
        onNewCpuHasGpuChange = { hasGpu -> viewModel.updateNewCpuHasGpu(hasGpu) },
        onNewCpuGpuNameChange = { gpuName -> viewModel.updateNewCpuGpuName(gpuName) },
        onNewCpuDescriptionChange = { desc -> viewModel.updateNewCpuDescription(desc) },
        onNewCpuRamAlertChange = { alert -> viewModel.updateNewCpuRamAlert(alert) },
        onSaveNewCpu = { viewModel.saveNewCpu() }
    )
}

/**
 * Stateless Composable: No conoce el ViewModel ni el origen de datos.
 * Recibe únicamente el [RawTuneUiState] e invoca callbacks ante eventos del usuario.
 * Cumple con State Hoisting estricto y facilita pruebas y Previews.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RawTuneContent(
    uiState: RawTuneUiState,
    onCpuSelected: (CpuOption) -> Unit,
    onRamChanged: (Int) -> Unit,
    onRamTypeChanged: (String) -> Unit,
    onProfileNameChanged: (String) -> Unit,
    onAnalyze: () -> Unit,
    onSaveProfile: () -> Unit,
    onDeleteProfile: (String) -> Unit,
    onLoadProfile: (SavedHardwareProfile) -> Unit,
    onDismissMessage: () -> Unit,
    onOpenAddCpuDialog: () -> Unit,
    onCloseAddCpuDialog: () -> Unit,
    onDeleteCustomCpu: (String) -> Unit,
    onNewCpuNameChange: (String) -> Unit,
    onNewCpuCoresChange: (Int) -> Unit,
    onNewCpuThreadsChange: (Int) -> Unit,
    onNewCpuScoreChange: (Int) -> Unit,
    onNewCpuRamTypeChange: (String) -> Unit,
    onNewCpuHasGpuChange: (Boolean) -> Unit,
    onNewCpuGpuNameChange: (String) -> Unit,
    onNewCpuDescriptionChange: (String) -> Unit,
    onNewCpuRamAlertChange: (String) -> Unit,
    onSaveNewCpu: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Diálogo modal para agregar nuevo CPU
    AddCpuDialog(
        show = uiState.showAddCpuDialog,
        name = uiState.newCpuName,
        cores = uiState.newCpuCores,
        threads = uiState.newCpuThreads,
        score = uiState.newCpuScore,
        ramType = uiState.newCpuRamType,
        hasGpu = uiState.newCpuHasGpu,
        gpuName = uiState.newCpuGpuName,
        description = uiState.newCpuDescription,
        ramAlert = uiState.newCpuRamAlert,
        onNameChange = onNewCpuNameChange,
        onCoresChange = onNewCpuCoresChange,
        onThreadsChange = onNewCpuThreadsChange,
        onScoreChange = onNewCpuScoreChange,
        onRamTypeChange = onNewCpuRamTypeChange,
        onHasGpuChange = onNewCpuHasGpuChange,
        onGpuNameChange = onNewCpuGpuNameChange,
        onDescriptionChange = onNewCpuDescriptionChange,
        onRamAlertChange = onNewCpuRamAlertChange,
        onDismiss = onCloseAddCpuDialog,
        onConfirm = onSaveNewCpu
    )

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "RawTune Mobile",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Spacer(modifier = Modifier.height(4.dp))

            // Banner informativo en caso de notificaciones
            uiState.userMessage?.let { message ->
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                        Text(
                            text = message,
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                            modifier = Modifier.weight(1f)
                        )
                        TextButton(onClick = onDismissMessage) {
                            Text("OK")
                        }
                    }
                }
            }

            Text(
                text = "Configuración del Sistema",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )

            // Componente 1: Selector de Procesador con botón de Agregar CPU y Ficha Técnica
            CpuDropdownSelector(
                availableCpus = uiState.availableCpus,
                selectedCpu = uiState.selectedCpu,
                onCpuSelected = onCpuSelected,
                onOpenAddCpuDialog = onOpenAddCpuDialog,
                onDeleteCustomCpu = onDeleteCustomCpu
            )

            // Componente 2: Configuración de RAM
            RamConfigCard(
                ramGb = uiState.ramGb,
                ramType = uiState.ramType,
                onRamChanged = onRamChanged,
                onRamTypeChanged = onRamTypeChanged
            )

            // Componente 3: Botones de Análisis y Guardado
            SaveProfileSection(
                profileName = uiState.customProfileName,
                onProfileNameChanged = onProfileNameChanged,
                onAnalyze = onAnalyze,
                onSaveProfile = onSaveProfile
            )

            // Componente 4: Resultados del Diagnóstico con Alertas
            DiagnosticMetricsCard(
                result = uiState.analysisResult
            )

            // Componente 5: Historial y Persistencia
            SavedProfilesList(
                profiles = uiState.savedProfiles,
                onLoadProfile = onLoadProfile,
                onDeleteProfile = onDeleteProfile
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun RawTuneContentPreview() {
    val sampleCpu = CpuOption(
        id = "r5_5600",
        name = "AMD Ryzen 5 5600",
        cores = 6,
        threads = 12,
        baseScore = 74,
        recommendedRamType = "DDR4",
        hasIntegratedGpu = false,
        integratedGpuName = "Ninguno",
        description = "Excelente procesador calidad-precio para gaming y desarrollo.",
        ramRecommendation = "Recomendado 16GB a 32GB DDR4 a 3200MHz."
    )
    val sampleResult = AnalysisResult(
        score = 82,
        bandwidthGbps = 51.2,
        bottleneckPercentage = 15,
        status = "Rendimiento Óptimo",
        summary = "Excelente configuración balanceada para desarrollo móvil y multitarea.",
        recommendations = listOf(
            "Recomendado 16GB a 32GB DDR4 a 3200MHz.",
            "El balance entre CPU y memoria se encuentra dentro del rango óptimo."
        )
    )

    MaterialTheme {
        RawTuneContent(
            uiState = RawTuneUiState(
                availableCpus = listOf(sampleCpu),
                selectedCpu = sampleCpu,
                ramGb = 16,
                ramType = "DDR4",
                analysisResult = sampleResult,
                savedProfiles = listOf(
                    SavedHardwareProfile("1", "PC Estudio", "AMD Ryzen 5 5600", 16, "DDR4", 82, 15)
                )
            ),
            onCpuSelected = {},
            onRamChanged = {},
            onRamTypeChanged = {},
            onProfileNameChanged = {},
            onAnalyze = {},
            onSaveProfile = {},
            onDeleteProfile = {},
            onLoadProfile = {},
            onDismissMessage = {},
            onOpenAddCpuDialog = {},
            onCloseAddCpuDialog = {},
            onDeleteCustomCpu = {},
            onNewCpuNameChange = {},
            onNewCpuCoresChange = {},
            onNewCpuThreadsChange = {},
            onNewCpuScoreChange = {},
            onNewCpuRamTypeChange = {},
            onNewCpuHasGpuChange = {},
            onNewCpuGpuNameChange = {},
            onNewCpuDescriptionChange = {},
            onNewCpuRamAlertChange = {},
            onSaveNewCpu = {}
        )
    }
}
