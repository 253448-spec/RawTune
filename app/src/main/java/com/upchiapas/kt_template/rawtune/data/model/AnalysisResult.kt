package com.upchiapas.kt_template.rawtune.data.model

data class AnalysisResult(
    val score: Int,
    val bandwidthGbps: Double,
    val bottleneckPercentage: Int,
    val status: String,
    val summary: String,
    val recommendations: List<String>
)
