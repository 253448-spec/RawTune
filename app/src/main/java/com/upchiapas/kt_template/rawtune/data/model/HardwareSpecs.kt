package com.upchiapas.kt_template.rawtune.data.model

data class HardwareSpecs(
    val cpu: CpuOption,
    val ramGb: Int,
    val ramType: String // "DDR4" o "DDR5"
)
