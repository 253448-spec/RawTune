package com.upchiapas.kt_template.rawtune.data.model

data class CpuOption(
    val id: String,
    val name: String,
    val cores: Int,
    val threads: Int,
    val baseScore: Int,
    val recommendedRamType: String
)
