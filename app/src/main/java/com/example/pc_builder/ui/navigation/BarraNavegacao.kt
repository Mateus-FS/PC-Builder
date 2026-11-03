package com.example.pc_builder.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

private data class Destino(val rota: String, val rotulo: String, val icone: ImageVector)

private val destinos = listOf(
    Destino(Rotas.LISTA, "Catálogo", Icons.AutoMirrored.Filled.List),
    Destino(Rotas.MONTAGENS, "Montagens", Icons.Default.Build),
    Destino(Rotas.CONFIGURACOES, "Configurações", Icons.Default.Settings)
)

@Composable
fun BarraNavegacao(
    rotaAtual: String,
    onNavegar: (String) -> Unit
) {
    val cores = MaterialTheme.colorScheme

    NavigationBar(containerColor = cores.surface) {
        destinos.forEach { destino ->
            NavigationBarItem(
                selected = destino.rota == rotaAtual,
                onClick = { if (destino.rota != rotaAtual) onNavegar(destino.rota) },
                icon = { Icon(destino.icone, contentDescription = null) },
                label = { Text(destino.rotulo) },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = cores.primaryContainer,
                    selectedIconColor = cores.onPrimaryContainer,
                    selectedTextColor = cores.onSurface,
                    unselectedIconColor = cores.onSurfaceVariant,
                    unselectedTextColor = cores.onSurfaceVariant
                )
            )
        }
    }
}