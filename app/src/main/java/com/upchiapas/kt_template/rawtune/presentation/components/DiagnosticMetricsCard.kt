package com.upchiapas.kt_template.rawtune.presentation.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.upchiapas.kt_template.rawtune.data.model.AnalysisResult

@Composable
fun DiagnosticMetricsCard(
    result: AnalysisResult?,
    modifier: Modifier = Modifier
) {
    if (result == null) return

    val scoreColor by animateColorAsState(
        targetValue = when {
            result.score >= 80 -> Color(0xFF2E7D32)
            result.score >= 60 -> Color(0xFFF57C00)
            else -> Color(0xFFC62828)
        },
        label = "scoreColor"
    )

    ElevatedCard(
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Speed,
                        contentDescription = "Puntuación",
                        tint = scoreColor
                    )
                    Text(
                        text = "Diagnóstico de Rendimiento",
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp
                    )
                }

                AssistChip(
                    onClick = {},
                    label = { Text(result.status, fontSize = 12.sp, fontWeight = FontWeight.SemiBold) }
                )
            }

            // Barra de progreso y puntaje
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Puntuación Global", fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(
                        text = "${result.score} / 100",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 18.sp,
                        color = scoreColor
                    )
                }
                LinearProgressIndicator(
                    progress = { (result.score / 100f).coerceIn(0f, 1f) },
                    modifier = Modifier.fillMaxWidth().height(10.dp),
                    color = scoreColor,
                    trackColor = scoreColor.copy(alpha = 0.2f),
                )
            }

            // Métricas secundarias
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("Ancho de Banda", fontSize = 12.sp, color = MaterialTheme.colorScheme.outline)
                    Text(
                        text = "${result.bandwidthGbps} GB/s",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text("Cuello de Botella", fontSize = 12.sp, color = MaterialTheme.colorScheme.outline)
                    Text(
                        text = "${result.bottleneckPercentage}%",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = if (result.bottleneckPercentage > 25) Color(0xFFC62828) else Color(0xFF2E7D32)
                    )
                }
            }

            HorizontalDivider()

            Text(
                text = result.summary,
                fontSize = 13.sp,
                lineHeight = 18.sp,
                color = MaterialTheme.colorScheme.onSurface
            )

            // Recomendaciones
            if (result.recommendations.isNotEmpty()) {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        text = "Recomendaciones Técnicas:",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    result.recommendations.forEach { rec ->
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Icon(
                                imageVector = if (result.bottleneckPercentage > 25) Icons.Default.Warning else Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = if (result.bottleneckPercentage > 25) Color(0xFFF57C00) else Color(0xFF2E7D32),
                                modifier = Modifier.size(16.dp).padding(top = 2.dp)
                            )
                            Text(text = rec, fontSize = 12.sp, lineHeight = 16.sp)
                        }
                    }
                }
            }
        }
    }
}
