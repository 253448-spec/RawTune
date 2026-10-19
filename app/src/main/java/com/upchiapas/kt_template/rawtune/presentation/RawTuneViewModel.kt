package com.upchiapas.kt_template.rawtune.presentation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class RawTuneUiState(
    val cpu: String = "AMD Ryzen 5 5600",
    val ram: Int = 16,
    val tipoRam: String = "DDR4",
    val puntuacion: Int = 0,
    val anchoBanda: Double = 0.0,
    val cuelloBotonella: Int = 0,
    val resumen: String = "Presiona 'Analizar Equipo' para generar el diagnóstico."
)

class RawTuneViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(RawTuneUiState())
    val uiState: StateFlow<RawTuneUiState> = _uiState.asStateFlow()

    fun actualizarRam(nuevaRam: Int) {
        _uiState.value = _uiState.value.copy(ram = nuevaRam)
    }

    fun actualizarRamTipo(nuevoTipo: String) {
        _uiState.value = _uiState.value.copy(tipoRam = nuevoTipo)
    }

    fun analizarHardware() {
        val estadoActual = _uiState.value
        var puntaje = 50
        var ancho = estadoActual.ram * 3.2
        var cuello = 30

        if (estadoActual.tipoRam == "DDR5") {
            puntaje += 20
            ancho += 15.0
        } else {
            puntaje += 10
        }

        if (estadoActual.ram >= 32) {
            puntaje += 20
            cuello -= 10
        } else if (estadoActual.ram >= 16) {
            puntaje += 10
            cuello -= 5
        }

        val mensaje = if (puntaje >= 80) {
            "Equipo excelente para desarrollo y multitarea."
        } else if (puntaje >= 60) {
            "Equipo funcional. Se recomienda ampliar RAM si es posible."
        } else {
            "Equipo limitado. Considera actualizar componentes para mejor rendimiento."
        }

        _uiState.value = estadoActual.copy(
            puntuacion = puntaje,
            anchoBanda = ancho,
            cuelloBotonella = cuello,
            resumen = mensaje
        )
    }
}