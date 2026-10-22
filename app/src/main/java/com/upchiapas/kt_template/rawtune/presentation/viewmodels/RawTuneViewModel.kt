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
    }

    fun updateRam(ramGb: Int) {
        _uiState.update { it.copy(ramGb = ramGb, userMessage = null) }
    }

    fun updateRamType(ramType: String) {
        _uiState.update { it.copy(ramType = ramType, userMessage = null) }
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
                userMessage = "Diagnóstico actualizado correctamente."
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
                userMessage = "¡Perfil '$profileName' guardado con éxito!"
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
                userMessage = "Perfil '${profile.name}' cargado en el simulador."
            )
        }
        analyzeHardware()
    }

    fun clearUserMessage() {
        _uiState.update { it.copy(userMessage = null) }
    }

    // Funciones de compatibilidad hacia atrás
    fun actualizarRam(nuevaRam: Int) = updateRam(nuevaRam)
    fun actualizarRamTipo(nuevoTipo: String) = updateRamType(nuevoTipo)
    fun analizarHardware() = analyzeHardware()
}
