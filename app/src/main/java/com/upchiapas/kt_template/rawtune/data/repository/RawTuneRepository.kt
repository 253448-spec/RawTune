package com.upchiapas.kt_template.rawtune.data.repository

import com.upchiapas.kt_template.rawtune.data.model.AnalysisResult
import com.upchiapas.kt_template.rawtune.data.model.CpuOption
import com.upchiapas.kt_template.rawtune.data.model.HardwareSpecs
import com.upchiapas.kt_template.rawtune.data.model.SavedHardwareProfile

interface RawTuneRepository {
    fun getAvailableCpus(): List<CpuOption>
    fun getDefaultSpecs(): HardwareSpecs
    fun analyzeHardware(specs: HardwareSpecs): AnalysisResult
    fun getSavedProfiles(): List<SavedHardwareProfile>
    fun saveProfile(profile: SavedHardwareProfile)
    fun deleteProfile(profileId: String)
}
