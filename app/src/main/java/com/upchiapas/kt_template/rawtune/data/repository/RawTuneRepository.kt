package com.upchiapas.kt_template.rawtune.data.repository

import com.upchiapas.kt_template.rawtune.data.model.AnalysisResult
import com.upchiapas.kt_template.rawtune.data.model.CpuOption
import com.upchiapas.kt_template.rawtune.data.model.HardwareSpecs

interface RawTuneRepository {
    fun getAvailableCpus(): List<CpuOption>
    fun getDefaultSpecs(): HardwareSpecs
    fun analyzeHardware(specs: HardwareSpecs): AnalysisResult
}
