package com.upchiapas.kt_template.rawtune.data.datasource

import com.upchiapas.kt_template.rawtune.data.model.AnalysisResult
import com.upchiapas.kt_template.rawtune.data.model.CpuOption
import com.upchiapas.kt_template.rawtune.data.model.HardwareSpecs
import java.util.Locale

class RawTuneLocalDataSource {

    private val cpuList = listOf(
        CpuOption(
            id = "r3_3200g",
            name = "AMD Ryzen 3 3200G",
            cores = 4,
            threads = 4,
            baseScore = 45,
            recommendedRamType = "DDR4"
        ),
        CpuOption(
            id = "r5_5600",
            name = "AMD Ryzen 5 5600",
            cores = 6,
            threads = 12,
            baseScore = 70,
            recommendedRamType = "DDR4"
        ),
        CpuOption(
            id = "i5_12400",
            name = "Intel Core i5-12400",
            cores = 6,
            threads = 12,
            baseScore = 72,
            recommendedRamType = "DDR4"
        ),
        CpuOption(
            id = "r7_7800x3d",
            name = "AMD Ryzen 7 7800X3D",
            cores = 8,
            threads = 16,
            baseScore = 92,
            recommendedRamType = "DDR5"
        ),
        CpuOption(
            id = "i7_13700k",
            name = "Intel Core i7-13700K",
            cores = 16,
            threads = 24,
            baseScore = 95,
            recommendedRamType = "DDR5"
        )
    )

    fun getCpuList(): List<CpuOption> = cpuList.toList()

    fun getDefaultSpecs(): HardwareSpecs {
        val defaultCpu = cpuList.firstOrNull { it.id == "r5_5600" } ?: cpuList.first()
        return HardwareSpecs(
            cpu = defaultCpu,
            ramGb = 16,
            ramType = "DDR4"
        )
    }

    fun calculateAnalysis(specs: HardwareSpecs): AnalysisResult {
        var score = specs.cpu.baseScore
        var bottleneck = 35

        val bandwidth = if (specs.ramType == "DDR5") {
            specs.ramGb * 4.8 + 15.0
        } else {
            specs.ramGb * 3.2
        }

        if (specs.ramType == "DDR5") {
            score += 10
            bottleneck -= 10
        } else {
            score += 5
        }

        if (specs.ramGb >= 32) {
            score += 15
            bottleneck -= 15
        } else if (specs.ramGb >= 16) {
            score += 8
            bottleneck -= 8
        } else {
            bottleneck += 15
        }

        val finalScore = score.coerceIn(10, 100)
        val finalBottleneck = bottleneck.coerceIn(5, 80)
        val roundedBandwidth = String.format(Locale.US, "%.1f", bandwidth).toDoubleOrNull() ?: bandwidth

        val status = when {
            finalScore >= 85 -> "Rendimiento Excelente"
            finalScore >= 65 -> "Rendimiento Óptimo"
            finalScore >= 45 -> "Rendimiento Aceptable"
            else -> "Rendimiento Limitado"
        }

        val summary = when {
            finalScore >= 85 -> "Configuración balanceada y de alto desempeño para gaming pesado y desarrollo multitarea."
            finalScore >= 65 -> "Excelente para productividad, desarrollo Android y multitarea general sin pérdidas notorias."
            finalScore >= 45 -> "Equipo funcional para tareas básicas o intermedias; puede experimentar saturación en cargas pesadas."
            else -> "Equipo con componentes desbalanceados. Se recomienda actualizar memoria y procesador."
        }

        val recommendations = mutableListOf<String>()
        if (specs.ramGb < 16) {
            recommendations.add("Aumentar la RAM a mínimo 16 GB para evitar cuellos de botella severos.")
        }
        if (specs.ramType == "DDR4" && specs.cpu.recommendedRamType == "DDR5") {
            recommendations.add("Tu procesador rendiría significativamente mejor con memoria DDR5.")
        }
        if (finalBottleneck > 25) {
            recommendations.add("Existe un margen de cuello de botella estimado del $finalBottleneck%. Revisa la velocidad y canales de RAM.")
        } else {
            recommendations.add("El balance entre CPU y memoria se encuentra dentro del rango óptimo.")
        }

        return AnalysisResult(
            score = finalScore,
            bandwidthGbps = roundedBandwidth,
            bottleneckPercentage = finalBottleneck,
            status = status,
            summary = summary,
            recommendations = recommendations
        )
    }
}
