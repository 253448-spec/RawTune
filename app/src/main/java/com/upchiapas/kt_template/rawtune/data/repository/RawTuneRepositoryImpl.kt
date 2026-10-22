package com.upchiapas.kt_template.rawtune.data.repository

import com.upchiapas.kt_template.rawtune.data.datasource.RawTuneLocalDataSource
import com.upchiapas.kt_template.rawtune.data.model.AnalysisResult
import com.upchiapas.kt_template.rawtune.data.model.CpuOption
import com.upchiapas.kt_template.rawtune.data.model.HardwareSpecs
import com.upchiapas.kt_template.rawtune.data.model.SavedHardwareProfile

class RawTuneRepositoryImpl(
    private val dataSource: RawTuneLocalDataSource
) : RawTuneRepository {

    override fun getAvailableCpus(): List<CpuOption> {
        return dataSource.getCpuList()
    }

    override fun getDefaultSpecs(): HardwareSpecs {
        return dataSource.getDefaultSpecs()
    }

    override fun analyzeHardware(specs: HardwareSpecs): AnalysisResult {
        return dataSource.calculateAnalysis(specs)
    }

    override fun getSavedProfiles(): List<SavedHardwareProfile> {
        return dataSource.getSavedProfiles()
    }

    override fun saveProfile(profile: SavedHardwareProfile) {
        dataSource.saveProfile(profile)
    }

    override fun deleteProfile(profileId: String) {
        dataSource.deleteProfile(profileId)
    }

    override fun addCustomCpu(cpu: CpuOption) {
        dataSource.saveCustomCpu(cpu)
    }

    override fun deleteCustomCpu(cpuId: String) {
        dataSource.deleteCustomCpu(cpuId)
    }
}
