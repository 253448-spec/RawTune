package com.upchiapas.kt_template.rawtune.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.upchiapas.kt_template.rawtune.data.repository.RawTuneRepository
import com.upchiapas.kt_template.rawtune.presentation.RawTuneViewModel

class RawTuneViewModelFactory(
    private val repository: RawTuneRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RawTuneViewModel::class.java)) {
            return RawTuneViewModel(repository) as T
        }
        throw IllegalArgumentException("Clase ViewModel desconocida")
    }
}
