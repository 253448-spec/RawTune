package com.upchiapas.kt_template

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.upchiapas.kt_template.rawtune.presentation.screens.RawTunePage
import com.upchiapas.kt_template.ui.theme.Kt_templateTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Kt_templateTheme {
                RawTunePage()
            }
        }
    }
}
