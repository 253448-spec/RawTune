package com.upchiapas.kt_template.scaffold.presentation.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.launch

/**
 * ScaffoldScreen Template
 * 
 * Este archivo sirve como una plantilla base que demuestra todos los slots y funciones
 * disponibles en el composable [Scaffold] de Material 3. Puede ser copiado y adaptado
 * para nuevos features del proyecto.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScaffoldScreen(
    onNavigationBack: () -> Unit = {},
    onActionClick: (String) -> Unit = {},
) {
    // Estado para el control de Snackbars (Mensajes flotantes)
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    
    // Estado de ejemplo para el Bottom Navigation Bar
    var selectedItem by remember { mutableIntStateOf(0) }
    val items = listOf("Inicio", "Perfil", "Ajustes")
    val icons = listOf(Icons.Filled.Home, Icons.Filled.Person, Icons.Filled.Settings)

    Scaffold(
        // 1. TOP BAR: Barra superior de la pantalla (TopAppBar, CenterAlignedTopAppBar, etc.)
        topBar = {
            TopAppBar(
                title = { Text(text = "Título del Feature") },
                navigationIcon = {
                    IconButton(onClick = onNavigationBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Regresar",
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { onActionClick("Menu") }) {
                        Icon(
                            imageVector = Icons.Filled.Menu,
                            contentDescription = "Más opciones",
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                )
            )
        },

        // 2. BOTTOM BAR: Barra inferior (NavigationBar o BottomAppBar)
        bottomBar = {
            NavigationBar {
                items.forEachIndexed { index, item ->
                    NavigationBarItem(
                        icon = { Icon(icons[index], contentDescription = item) },
                        label = { Text(item) },
                        selected = selectedItem == index,
                        onClick = { selectedItem = index }
                    )
                }
            }
        },

        // 3. SNACKBAR HOST: Contenedor para mostrar notificaciones tipo Snackbar
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },

        // 4. FLOATING ACTION BUTTON (FAB): Botón de acción flotante principal
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar(
                            message = "Acción del FAB ejecutada"
                        )
                    }
                }
            ) {
                Icon(imageVector = Icons.Filled.Add, contentDescription = "Agregar")
            }
        },

        // 5. FAB POSITION: Ubicación del FAB en la pantalla (End, Center)
        floatingActionButtonPosition = FabPosition.End,

        // 6. CONTAINER COLOR & CONTENT COLOR: Personalización de colores de fondo y contenido
        containerColor = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.onBackground,

        // 7. WINDOW INSETS: Manejo automático de áreas del sistema (ej. barra de estado/navegación)
        // Por defecto ya incluye ScaffoldDefaults.contentWindowInsets
    ) { paddingValues ->
        
        // 8. CONTENT: Contenido principal de la pantalla. 
        // ¡IMPORTANTE! Es obligatorio aplicar paddingValues al contenedor principal para evitar 
        // que las barras superior e inferior cubran el contenido.
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues), // <- Aplicación del padding obligatorio
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Contenido de la sección: ${items[selectedItem]}",
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ScaffoldScreenPreview() {
    MaterialTheme {
        ScaffoldScreen()
    }
}
