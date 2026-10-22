package com.upchiapas.kt_template.rawtune.data.datasource

import android.content.Context
import android.content.SharedPreferences
import com.upchiapas.kt_template.rawtune.data.model.AnalysisResult
import com.upchiapas.kt_template.rawtune.data.model.CpuOption
import com.upchiapas.kt_template.rawtune.data.model.HardwareSpecs
import com.upchiapas.kt_template.rawtune.data.model.SavedHardwareProfile
import java.util.Locale

class RawTuneLocalDataSource(
    context: Context? = null
) {
    private val prefs: SharedPreferences? = context?.getSharedPreferences("rawtune_storage", Context.MODE_PRIVATE)

    // Catálogo base de procesadores (Datos semilla predefinidos)
    private val defaultCpuList = listOf(
        CpuOption(
            id = "r3_3200g",
            name = "AMD Ryzen 3 3200G",
            cores = 4,
            threads = 4,
            baseScore = 48,
            recommendedRamType = "DDR4",
            hasIntegratedGpu = true,
            integratedGpuName = "Radeon Vega 8 Graphics",
            description = "APU de entrada ideal para ofimática, tareas escolares y títulos esports como LoL o Valorant sin gráfica dedicada.",
            ramRecommendation = "⚠️ Requiere obligatoriamente memoria RAM en Dual-Channel (mínimo 16GB a 3200MHz) para que la GPU integrada no pierda hasta el 40% de potencia."
        ),
        CpuOption(
            id = "r5_5600",
            name = "AMD Ryzen 5 5600",
            cores = 6,
            threads = 12,
            baseScore = 74,
            recommendedRamType = "DDR4",
            hasIntegratedGpu = false,
            integratedGpuName = "Ninguno (Requiere gráfica dedicada)",
            description = "Excelente procesador calidad-precio para gaming 1080p competitivo y desarrollo de software.",
            ramRecommendation = "Recomendado 16GB a 32GB DDR4 a 3200 o 3600 MHz CL16 para latencias ultrabajas."
        ),
        CpuOption(
            id = "i5_12400",
            name = "Intel Core i5-12400",
            cores = 6,
            threads = 12,
            baseScore = 75,
            recommendedRamType = "DDR4",
            hasIntegratedGpu = true,
            integratedGpuName = "Intel UHD Graphics 730",
            description = "Procesador todoterreno con QuickSync, muy eficiente para edición ligera de video y productividad diaria.",
            ramRecommendation = "Funciona con DDR4 o DDR5. Se recomienda mínimo 16GB para trabajo multitarea sin saturación."
        ),
        CpuOption(
            id = "r7_7800x3d",
            name = "AMD Ryzen 7 7800X3D",
            cores = 8,
            threads = 16,
            baseScore = 94,
            recommendedRamType = "DDR5",
            hasIntegratedGpu = true,
            integratedGpuName = "Radeon Graphics (Básica 2 CU)",
            description = "El procesador gaming de mayor rendimiento por su enorme caché 3D V-Cache; máxima estabilidad de FPS.",
            ramRecommendation = "Requiere memoria DDR5 rápida: el punto dulce son 32GB DDR5 a 6000MHz con latencia CL30 y perfil EXPO activo."
        ),
        CpuOption(
            id = "i7_13700k",
            name = "Intel Core i7-13700K",
            cores = 16,
            threads = 24,
            baseScore = 96,
            recommendedRamType = "DDR5",
            hasIntegratedGpu = true,
            integratedGpuName = "Intel UHD Graphics 770",
            description = "Potencia bruta híbrida (Performance + Efficient cores) para renderizado 3D, desarrollo móvil pesado y streaming simultáneo.",
            ramRecommendation = "Altamente demandante de memoria. Se recomiendan de 32GB a 64GB DDR5 para proyectos complejos en Android Studio."
        )
    )

    // Perfiles semilla en memoria en caso de arranque inicial
    private val seedProfiles = mutableListOf(
        SavedHardwareProfile(
            id = "seed_1",
            name = "Ensamble Base Oficina (Semilla)",
            cpuName = "AMD Ryzen 3 3200G",
            ramGb = 8,
            ramType = "DDR4",
            score = 52,
            bottleneck = 35
        ),
        SavedHardwareProfile(
            id = "seed_2",
            name = "PC Gaming Recomendada (Semilla)",
            cpuName = "AMD Ryzen 5 5600",
            ramGb = 16,
            ramType = "DDR4",
            score = 80,
            bottleneck = 15
        )
    )

    // Memoria en caso de no contar con contexto (tests o previews)
    private val memoryCustomCpus = mutableListOf<CpuOption>()

    // Obtener catálogo completo (Datos semilla + Procesadores creados por el usuario)
    fun getCpuList(): List<CpuOption> {
        val sp = prefs
        if (sp == null) {
            return (defaultCpuList + memoryCustomCpus).distinctBy { it.id }
        }

        val rawIds = sp.getString("custom_cpu_ids_list", null)
        val loadedCustomCpus = mutableListOf<CpuOption>()

        if (!rawIds.isNullOrBlank()) {
            val idList = rawIds.split(",").filter { it.isNotBlank() }
            for (id in idList) {
                val name = sp.getString("cpu_${id}_name", null) ?: continue
                val cores = sp.getInt("cpu_${id}_cores", 6)
                val threads = sp.getInt("cpu_${id}_threads", 12)
                val score = sp.getInt("cpu_${id}_score", 70)
                val ramType = sp.getString("cpu_${id}_ram_type", "DDR4") ?: "DDR4"
                val hasGpu = sp.getBoolean("cpu_${id}_has_gpu", false)
                val gpuName = sp.getString("cpu_${id}_gpu_name", "Ninguno") ?: "Ninguno"
                val desc = sp.getString("cpu_${id}_desc", "CPU personalizado añadido por el usuario.") ?: ""
                val ramAlert = sp.getString("cpu_${id}_ram_alert", "Configuración de RAM definida por el usuario.") ?: ""

                loadedCustomCpus.add(
                    CpuOption(
                        id = id,
                        name = name,
                        cores = cores,
                        threads = threads,
                        baseScore = score,
                        recommendedRamType = ramType,
                        hasIntegratedGpu = hasGpu,
                        integratedGpuName = gpuName,
                        description = desc,
                        ramRecommendation = ramAlert
                    )
                )
            }
        }

        return (defaultCpuList + loadedCustomCpus).distinctBy { it.id }
    }

    // Guardar nuevo CPU personalizado introducido por el usuario (Persistencia SharedPreferences)
    fun saveCustomCpu(cpu: CpuOption) {
        val sp = prefs
        if (sp == null) {
            memoryCustomCpus.removeAll { it.id == cpu.id }
            memoryCustomCpus.add(0, cpu)
            return
        }

        val rawIds = sp.getString("custom_cpu_ids_list", "") ?: ""
        val existingIds = rawIds.split(",").filter { it.isNotBlank() }.toMutableList()
        if (!existingIds.contains(cpu.id)) {
            existingIds.add(0, cpu.id)
        }

        sp.edit()
            .putString("custom_cpu_ids_list", existingIds.joinToString(","))
            .putString("cpu_${cpu.id}_name", cpu.name)
            .putInt("cpu_${cpu.id}_cores", cpu.cores)
            .putInt("cpu_${cpu.id}_threads", cpu.threads)
            .putInt("cpu_${cpu.id}_score", cpu.baseScore)
            .putString("cpu_${cpu.id}_ram_type", cpu.recommendedRamType)
            .putBoolean("cpu_${cpu.id}_has_gpu", cpu.hasIntegratedGpu)
            .putString("cpu_${cpu.id}_gpu_name", cpu.integratedGpuName)
            .putString("cpu_${cpu.id}_desc", cpu.description)
            .putString("cpu_${cpu.id}_ram_alert", cpu.ramRecommendation)
            .apply()
    }

    fun deleteCustomCpu(cpuId: String) {
        val sp = prefs
        if (sp == null) {
            memoryCustomCpus.removeAll { it.id == cpuId }
            return
        }

        val rawIds = sp.getString("custom_cpu_ids_list", "") ?: ""
        val existingIds = rawIds.split(",").filter { it.isNotBlank() && it != cpuId }

        sp.edit()
            .putString("custom_cpu_ids_list", existingIds.joinToString(","))
            .remove("cpu_${cpuId}_name")
            .remove("cpu_${cpuId}_cores")
            .remove("cpu_${cpuId}_threads")
            .remove("cpu_${cpuId}_score")
            .remove("cpu_${cpuId}_ram_type")
            .remove("cpu_${cpuId}_has_gpu")
            .remove("cpu_${cpuId}_gpu_name")
            .remove("cpu_${cpuId}_desc")
            .remove("cpu_${cpuId}_ram_alert")
            .apply()

        memoryCustomCpus.removeAll { it.id == cpuId }
    }

    fun getDefaultSpecs(): HardwareSpecs {
        val all = getCpuList()
        val defaultCpu = all.firstOrNull { it.id == "r5_5600" } ?: all.first()
        return HardwareSpecs(
            cpu = defaultCpu,
            ramGb = 16,
            ramType = "DDR4"
        )
    }

    // Cálculo y análisis de hardware con consideraciones de GPU integrado y alertas de RAM
    fun calculateAnalysis(specs: HardwareSpecs): AnalysisResult {
        var score = specs.cpu.baseScore
        var bottleneck = 32

        val bandwidth = if (specs.ramType == "DDR5") {
            specs.ramGb * 4.8 + 15.0
        } else {
            specs.ramGb * 3.2
        }

        if (specs.ramType == "DDR5") {
            score += 10
            bottleneck -= 8
        } else {
            score += 4
        }

        if (specs.ramGb >= 32) {
            score += 16
            bottleneck -= 14
        } else if (specs.ramGb >= 16) {
            score += 8
            bottleneck -= 7
        } else {
            bottleneck += 18
        }

        // Penalizaciones o bonificaciones por GPU Integrado
        if (specs.cpu.hasIntegratedGpu) {
            if (specs.ramGb < 16) {
                // Al compartir RAM con la GPU integrada, menos de 16GB colapsa el rendimiento
                score -= 10
                bottleneck += 15
            } else {
                score += 4
            }
        }

        val finalScore = score.coerceIn(10, 100)
        val finalBottleneck = bottleneck.coerceIn(5, 85)
        val roundedBandwidth = String.format(Locale.US, "%.1f", bandwidth).toDoubleOrNull() ?: bandwidth

        val status = when {
            finalScore >= 85 -> "Rendimiento Excelente"
            finalScore >= 65 -> "Rendimiento Óptimo"
            finalScore >= 45 -> "Rendimiento Aceptable"
            else -> "Rendimiento Limitado"
        }

        val summary = when {
            finalScore >= 85 -> "Configuración de alto desempeño balanceada para gaming avanzado, streaming y emulación Android."
            finalScore >= 65 -> "Excelente balance para productividad, desarrollo móvil y multitarea general sin caídas de rendimiento."
            finalScore >= 45 -> "Equipo funcional para tareas intermedias; puede presentar saturación en compilación pesada."
            else -> "Equipo con componentes descompensados. Se requiere ampliación de memoria para evitar saturación."
        }

        val recommendations = mutableListOf<String>()

        // 1. Alerta específica del CPU elegido
        if (specs.cpu.ramRecommendation.isNotBlank()) {
            recommendations.add(specs.cpu.ramRecommendation)
        }

        // 2. Alerta de GPU integrado compartiendo memoria
        if (specs.cpu.hasIntegratedGpu) {
            if (specs.ramGb < 16) {
                recommendations.add("🚨 ALERTA GPU: Este procesador usa '${specs.cpu.integratedGpuName}' y toma memoria de la RAM física. Con solo ${specs.ramGb}GB el sistema sufrirá tartamudeo severo.")
            } else {
                recommendations.add("🎮 Gráficos integrados activos (${specs.cpu.integratedGpuName}). Se asignan 2GB a 4GB de RAM como VRAM compartida.")
            }
        }

        // 3. Recomendación de tecnología DDR4 vs DDR5
        if (specs.ramType == "DDR4" && specs.cpu.recommendedRamType == "DDR5") {
            recommendations.add("⚠️ Tu procesador '${specs.cpu.name}' aprovecha mejor plataformas DDR5 para maximizar el ancho de banda.")
        }

        // 4. Diagnóstico de cuello de botella
        if (finalBottleneck > 25) {
            recommendations.add("Cuello de botella estimado en $finalBottleneck%. Revisa la configuración de canales de RAM.")
        } else {
            recommendations.add("✅ El balance entre núcleos de CPU y capacidad de memoria se encuentra dentro del rango óptimo.")
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

    // Persistencia de ensambles/perfiles con SharedPreferences
    fun getSavedProfiles(): List<SavedHardwareProfile> {
        val sp = prefs ?: return seedProfiles.toList()
        val rawIds = sp.getString("profile_ids_list", null)
        if (rawIds.isNullOrBlank()) {
            return seedProfiles.toList()
        }

        val idList = rawIds.split(",").filter { it.isNotBlank() }
        val loadedProfiles = mutableListOf<SavedHardwareProfile>()

        for (id in idList) {
            val name = sp.getString("prof_${id}_name", null) ?: continue
            val cpu = sp.getString("prof_${id}_cpu", "AMD Ryzen 5 5600") ?: "AMD Ryzen 5 5600"
            val ram = sp.getInt("prof_${id}_ram", 16)
            val type = sp.getString("prof_${id}_type", "DDR4") ?: "DDR4"
            val score = sp.getInt("prof_${id}_score", 70)
            val bottleneck = sp.getInt("prof_${id}_bottleneck", 20)
            val time = sp.getLong("prof_${id}_time", System.currentTimeMillis())

            loadedProfiles.add(
                SavedHardwareProfile(
                    id = id,
                    name = name,
                    cpuName = cpu,
                    ramGb = ram,
                    ramType = type,
                    score = score,
                    bottleneck = bottleneck,
                    timestamp = time
                )
            )
        }

        return (loadedProfiles + seedProfiles).distinctBy { it.id }
    }

    fun saveProfile(profile: SavedHardwareProfile) {
        val sp = prefs
        if (sp == null) {
            seedProfiles.add(0, profile)
            return
        }

        val rawIds = sp.getString("profile_ids_list", "") ?: ""
        val existingIds = rawIds.split(",").filter { it.isNotBlank() }.toMutableList()
        if (!existingIds.contains(profile.id)) {
            existingIds.add(0, profile.id)
        }

        sp.edit()
            .putString("profile_ids_list", existingIds.joinToString(","))
            .putString("prof_${profile.id}_name", profile.name)
            .putString("prof_${profile.id}_cpu", profile.cpuName)
            .putInt("prof_${profile.id}_ram", profile.ramGb)
            .putString("prof_${profile.id}_type", profile.ramType)
            .putInt("prof_${profile.id}_score", profile.score)
            .putInt("prof_${profile.id}_bottleneck", profile.bottleneck)
            .putLong("prof_${profile.id}_time", profile.timestamp)
            .apply()
    }

    fun deleteProfile(profileId: String) {
        val sp = prefs
        if (sp == null) {
            seedProfiles.removeAll { it.id == profileId }
            return
        }

        val rawIds = sp.getString("profile_ids_list", "") ?: ""
        val existingIds = rawIds.split(",").filter { it.isNotBlank() && it != profileId }

        sp.edit()
            .putString("profile_ids_list", existingIds.joinToString(","))
            .remove("prof_${profileId}_name")
            .remove("prof_${profileId}_cpu")
            .remove("prof_${profileId}_ram")
            .remove("prof_${profileId}_type")
            .remove("prof_${profileId}_score")
            .remove("prof_${profileId}_bottleneck")
            .remove("prof_${profileId}_time")
            .apply()

        seedProfiles.removeAll { it.id == profileId }
    }
}
