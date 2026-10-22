package com.upchiapas.kt_template.rawtune.data.model

data class SavedHardwareProfile(
    val id: String,
    val name: String,
    val cpuName: String,
    val ramGb: Int,
    val ramType: String,
    val score: Int,
    val bottleneck: Int,
    val timestamp: Long = System.currentTimeMillis()
)
