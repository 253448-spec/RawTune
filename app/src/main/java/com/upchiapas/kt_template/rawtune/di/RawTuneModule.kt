package com.upchiapas.kt_template.rawtune.di

import com.upchiapas.kt_template.rawtune.data.datasource.RawTuneLocalDataSource
import com.upchiapas.kt_template.rawtune.data.repository.RawTuneRepository
import com.upchiapas.kt_template.rawtune.data.repository.RawTuneRepositoryImpl
import com.upchiapas.kt_template.rawtune.presentation.viewmodels.RawTuneViewModelFactory

class RawTuneModule {
    private val localDataSource: RawTuneLocalDataSource by lazy {
        RawTuneLocalDataSource()
    }

    val repository: RawTuneRepository by lazy {
        RawTuneRepositoryImpl(localDataSource)
    }

    val factoryRawTune: RawTuneViewModelFactory by lazy {
        RawTuneViewModelFactory(repository)
    }
}
