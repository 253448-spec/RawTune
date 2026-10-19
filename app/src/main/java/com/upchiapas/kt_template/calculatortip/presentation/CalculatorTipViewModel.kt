package com.upchiapas.kt_template.calculatortip.presentation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class CalculatorTipViewModel : ViewModel() {
    private var _amountInput  = MutableStateFlow("")
    val amountInput: StateFlow<String> = _amountInput.asStateFlow()
    private var _tipInput  = MutableStateFlow("")
    val tipInput: StateFlow<String> = _tipInput.asStateFlow()
    private var _roundUp  = MutableStateFlow(false)
    val roundUp: StateFlow<Boolean> = _roundUp.asStateFlow()

    fun onAmountChange(amount: String) {
        _amountInput.value = amount
    }
    fun onTipChange(tip: String) {
        _tipInput.value = tip
    }
    fun onRoundUpChange(roundUp: Boolean) {
        //_roundUp.value = roundUp
        _roundUp.update { roundUp }
    }
}