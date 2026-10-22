package com.upchiapas.kt_template.rawtune.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BookmarkAdd
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun SaveProfileSection(
    profileName: String,
    onProfileNameChanged: (String) -> Unit,
    onAnalyze: () -> Unit,
    onSaveProfile: () -> Unit,
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Acciones y Registro Persistente",
                fontWeight = FontWeight.SemiBold
            )

            Button(
                onClick = onAnalyze,
                modifier = Modifier.fillMaxWidth().height(48.dp)
            ) {
                Icon(Icons.Default.PlayArrow, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("ANALIZAR HARDWARE", fontWeight = FontWeight.Bold)
            }

            OutlinedTextField(
                value = profileName,
                onValueChange = onProfileNameChanged,
                label = { Text("Nombre del Ensamble") },
                placeholder = { Text("Ej. Mi PC Gamer 2026") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            FilledTonalButton(
                onClick = onSaveProfile,
                modifier = Modifier.fillMaxWidth().height(46.dp)
            ) {
                Icon(Icons.Default.BookmarkAdd, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("GUARDAR EN MEMORIA LOCAL")
            }
        }
    }
}
