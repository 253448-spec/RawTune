package com.upchiapas.kt_template.quiz.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp

@Composable
fun ResultContent(
    score: Int,
    totalQuestions: Int,
    onRestart: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "¡Quiz Finalizado!",
            style = MaterialTheme.typography.headlineMedium
        )
        Text(
            text = "Tu puntuación: $score / $totalQuestions",
            style = MaterialTheme.typography.bodyLarge
        )
        Button(onClick = onRestart) {
            Text("Reiniciar")
        }
    }
}