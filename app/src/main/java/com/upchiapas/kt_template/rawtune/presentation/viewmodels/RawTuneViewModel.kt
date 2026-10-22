package com.upchiapas.kt_template.rawtune.presentation.viewmodels

import androidx.lifecycle.ViewModel
import com.upchiapas.kt_template.rawtune.data.model.CpuOption
import com.upchiapas.kt_template.rawtune.data.model.HardwareSpecs
import com.upchiapas.kt_template.rawtune.data.model.SavedHardwareProfile
import com.upchiapas.kt_template.rawtune.data.repository.RawTuneRepository
import com.upchiapas.kt_template.rawtune.presentation.screens.RawTuneUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.UUID

class RawTuneViewModel(
    private val repository: RawTuneRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(RawTuneUiState())
    val uiState: StateFlow<RawTuneUiState> = _uiState.asStateFlow()

    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        val cpus = repository.getAvailableCpus()
        val defaultSpecs = repository.getDefaultSpecs()
        val profiles = repository.getSavedProfiles()
        val initialAnalysis = repository.analyzeHardware(defaultSpecs)

        _uiState.update {
            it.copy(
                availableCpus = cpus,
                selectedCpu = defaultSpecs.cpu,
                ramGb = defaultSpecs.ramGb,
                ramType = defaultSpecs.ramType,
                analysisResult = initialAnalysis,
                savedProfiles = profiles
            )
        }
    }

    fun selectCpu(cpu: CpuOption) {
        _uiState.update { it.copy(selectedCpu = cpu, userMessage = null) }
        analyzeHardware()
    }

    fun updateRam(ramGb: Int) {
        _uiState.update { it.copy(ramGb = ramGb, userMessage = null) }
        analyzeHardware()
    }

    fun updateRamType(ramType: String) {
        _uiState.update { it.copy(ramType = ramType, userMessage = null) }
        analyzeHardware()
    }

    fun updateProfileName(name: String) {
        _uiState.update { it.copy(customProfileName = name, userMessage = null) }
    }

    fun analyzeHardware() {
        val current = _uiState.value
        val cpu = current.selectedCpu ?: return
        val specs = HardwareSpecs(
            cpu = cpu,
            ramGb = current.ramGb,
            ramType = current.ramType
        )
        val result = repository.analyzeHardware(specs)

        _uiState.update {
            it.copy(
                analysisResult = result,
                userMessage = "Diagnóstico actualizado para ${cpu.name}."
            )
        }
    }

    fun saveCurrentProfile() {
        val current = _uiState.value
        val cpu = current.selectedCpu ?: return
        val result = current.analysisResult ?: return
        val profileName = if (current.customProfileName.isNotBlank()) {
            current.customProfileName.trim()
        } else {
            "Perfil ${cpu.name} - ${current.ramGb}GB"
        }

        val newProfile = SavedHardwareProfile(
            id = UUID.randomUUID().toString(),
            name = profileName,
            cpuName = cpu.name,
            ramGb = current.ramGb,
            ramType = current.ramType,
            score = result.score,
            bottleneck = result.bottleneckPercentage
        )

        repository.saveProfile(newProfile)
        val updatedProfiles = repository.getSavedProfiles()

        _uiState.update {
            it.copy(
                savedProfiles = updatedProfiles,
                customProfileName = "",
                userMessage = "¡Ensamble '$profileName' guardado con éxito en memoria!"
            )
        }
    }

    fun deleteProfile(profileId: String) {
        repository.deleteProfile(profileId)
        val updatedProfiles = repository.getSavedProfiles()

        _uiState.update {
            it.copy(
                savedProfiles = updatedProfiles,
                userMessage = "Perfil eliminado del almacenamiento."
            )
        }
    }

    fun loadProfile(profile: SavedHardwareProfile) {
        val matchedCpu = _uiState.value.availableCpus.firstOrNull { it.name == profile.cpuName }
            ?: _uiState.value.selectedCpu

        _uiState.update {
            it.copy(
                selectedCpu = matchedCpu,
                ramGb = profile.ramGb,
                ramType = profile.ramType,
                customProfileName = profile.name,
                userMessage = "Ensamble '${profile.name}' cargado en el optimizador."
            )
        }
        analyzeHardware()
    }

    fun clearUserMessage() {
        _uiState.update { it.copy(userMessage = null) }
    }

    // ==========================================
    // GESTIÓN MANUAL DE NUEVOS PROCESADORES (CPU)
    // ==========================================
    fun toggleAddCpuDialog(show: Boolean) {
        _uiState.update { it.copy(showAddCpuDialog = show) }
    }

    fun updateNewCpuName(value: String) {
        _uiState.update { it.copy(newCpuName = value) }
    }

    fun updateNewCpuCores(value: Int) {
        _uiState.update { it.copy(newCpuCores = value) }
    }

    fun updateNewCpuThreads(value: Int) {
        _uiState.update { it.copy(newCpuThreads = value) }
    }

    fun updateNewCpuScore(value: Int) {
        _uiState.update { it.copy(newCpuScore = value) }
    }

    fun updateNewCpuRamType(value: String) {
        _uiState.update { it.copy(newCpuRamType = value) }
    }

    fun updateNewCpuHasGpu(value: Boolean) {
        _uiState.update { it.copy(newCpuHasGpu = value) }
    }

    fun updateNewCpuGpuName(value: String) {
        _uiState.update { it.copy(newCpuGpuName = value) }
    }

    fun updateNewCpuDescription(value: String) {
        _uiState.update { it.copy(newCpuDescription = value) }
    }

    fun updateNewCpuRamAlert(value: String) {
        _uiState.update { it.copy(newCpuRamAlert = value) }
    }

    fun saveNewCpu() {
        val current = _uiState.value
        val name = current.newCpuName.trim()
        if (name.isBlank()) {
            _uiState.update { it.copy(userMessage = "Por favor ingresa un nombre para el procesador.") }
            return
        }

        val gpuName = if (current.newCpuHasGpu) {
            if (current.newCpuGpuName.isNotBlank()) current.newCpuGpuName.trim() else "GPU Integrada Básica"
        } else {
            "Ninguno"
        }

        val description = if (current.newCpuDescription.isNotBlank()) {
            current.newCpuDescription.trim()
        } else {
            "Procesador personalizado de ${current.newCpuCores} núcleos y ${current.newCpuThreads} hilos."
        }

        val ramAlert = if (current.newCpuRamAlert.isNotBlank()) {
            current.newCpuRamAlert.trim()
        } else {
            "Recomendado mínimo 16GB ${current.newCpuRamType}."
        }

        val newCpu = CpuOption(
            id = "custom_${System.currentTimeMillis()}",
            name = name,
            cores = current.newCpuCores,
            threads = current.newCpuThreads,
            baseScore = current.newCpuScore,
            recommendedRamType = current.newCpuRamType,
            hasIntegratedGpu = current.newCpuHasGpu,
            integratedGpuName = gpuName,
            description = description,
            ramRecommendation = ramAlert
        )

        // Persistir en SharedPreferences a través del Repository
        repository.addCustomCpu(newCpu)
        val updatedCpus = repository.getAvailableCpus()

        _uiState.update {
            it.copy(
                availableCpus = updatedCpus,
                selectedCpu = newCpu,
                showAddCpuDialog = false,
                newCpuName = "",
                newCpuCores = 6,
                newCpuThreads = 12,
                newCpuScore = 75,
                newCpuRamType = "DDR4",
                newCpuHasGpu = false,
                newCpuGpuName = "",
                newCpuDescription = "",
                newCpuRamAlert = "",
                userMessage = "¡Procesador '$name' agregado y persistido en la app!"
            )
        }

        // Recalcular el análisis con el nuevo procesador seleccionado
        analyzeHardware()
    }

    fun deleteCustomCpu(cpuId: String) {
        repository.deleteCustomCpu(cpuId)
        val updatedCpus = repository.getAvailableCpus()
        val nextCpu = updatedCpus.firstOrNull()

        _uiState.update {
            it.copy(
                availableCpus = updatedCpus,
                selectedCpu = if (it.selectedCpu?.id == cpuId) nextCpu else it.selectedCpu,
                userMessage = "Procesador eliminado de la base local."
            )
        }
        analyzeHardware()
    }

    // Compatibilidad
    fun actualizarRam(nuevaRam: Int) = updateRam(nuevaRam)
    fun actualizarRamTipo(nuevoTipo: String) = updateRamType(nuevoTipo)
    fun analizarHardware() = analyzeHardware()
}
