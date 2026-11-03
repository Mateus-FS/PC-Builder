package com.example.pc_builder

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.pc_builder.ui.configuracoes.ConfiguracoesViewModel
import com.example.pc_builder.ui.navigation.AppNavigation
import com.example.pc_builder.ui.theme.PcBuilderTheme

class MainActivity : ComponentActivity() {

    private val configuracoesViewModel: ConfiguracoesViewModel by viewModels {
        ConfiguracoesViewModel.Factory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val configuracoes by configuracoesViewModel.configuracoes.collectAsStateWithLifecycle()
            val atuais = configuracoes

            if (atuais != null) {
                val escuro = atuais.temaEscuro ?: isSystemInDarkTheme()

                val view = LocalView.current
                if (!view.isInEditMode) {
                    SideEffect {
                        val window = (view.context as Activity).window
                        WindowCompat.getInsetsController(window, view).apply {
                            isAppearanceLightStatusBars = !escuro
                            isAppearanceLightNavigationBars = !escuro
                        }
                    }
                }

                PcBuilderTheme(darkTheme = escuro) {
                    Surface(modifier = Modifier.fillMaxSize()) {
                        AppNavigation()
                    }
                }
            }
        }
    }
}