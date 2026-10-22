package com.upchiapas.kt_template.rawtune.presentation.screens

import com.upchiapas.kt_template.rawtune.data.model.AnalysisResult
import com.upchiapas.kt_template.rawtune.data.model.CpuOption
import com.upchiapas.kt_template.rawtune.data.model.SavedHardwareProfile

data class RawTuneUiState(
    val availableCpus: List<CpuOption> = emptyList(),
    val selectedCpu: CpuOption? = null,
    val ramGb: Int = 16,
    val ramType: String = "DDR4",
    val customProfileName: String = "",
    val analysisResult: AnalysisResult? = null,
    val savedProfiles: List<SavedHardwareProfile> = emptyList(),
    val isAnalyzing: Boolean = false,
    val userMessage: String? = null
) {
    // Propiedades calculadas y de compatibilidad
    val cpu: String
        get() = selectedCpu?.name ?: "AMD Ryzen 5 5600"

    val ram: Int
        get() = ramGb

    val tipoRam: String
        get() = ramType

    val puntuacion: Int
        get() = analysisResult?.score ?: 0

    val anchoBanda: Double
        get() = analysisResult?.bandwidthGbps ?: 0.0

    val cuelloBotonella: Int
        get() = analysisResult?.bottleneckPercentage ?: 0

    val resumen: String
        get() = analysisResult?.summary ?: "Presiona 'Analizar Equipo' para generar el diagnóstico."
}
