package com.example.pc_builder

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
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
        setContent {
            val configuracoes by configuracoesViewModel.configuracoes.collectAsStateWithLifecycle()
            val atuais = configuracoes

            // Só desenha depois de ler a preferência, para não piscar o tema errado.
            if (atuais != null) {
                val escuro = atuais.temaEscuro ?: isSystemInDarkTheme()

                // Faz os ícones da barra de status/navegação acompanharem o tema do APP,
                // e não o do sistema (senão ficam invisíveis quando os dois divergem).
                DisposableEffect(escuro) {
                    enableEdgeToEdge(
                        statusBarStyle = SystemBarStyle.auto(
                            Color.TRANSPARENT, Color.TRANSPARENT
                        ) { _ -> escuro },
                        navigationBarStyle = SystemBarStyle.auto(
                            Color.argb(0xE6, 0xFF, 0xFF, 0xFF),
                            Color.argb(0x80, 0x1B, 0x1B, 0x1B)
                        ) { _ -> escuro }
                    )
                    onDispose { }
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