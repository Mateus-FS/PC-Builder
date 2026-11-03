package com.example.pc_builder.ui.configuracoes

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pc_builder.ui.navigation.BarraNavegacao
import com.example.pc_builder.ui.navigation.Rotas

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfiguracoesScreen(
    onNavegar: (String) -> Unit,
    viewModel: ConfiguracoesViewModel = viewModel(factory = ConfiguracoesViewModel.Factory)
) {
    val configuracoes by viewModel.configuracoes.collectAsStateWithLifecycle()
    val temaEscuro = configuracoes?.temaEscuro ?: isSystemInDarkTheme()
    val cores = MaterialTheme.colorScheme

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Configurações", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = cores.background)
            )
        },
        bottomBar = { BarraNavegacao(rotaAtual = Rotas.CONFIGURACOES, onNavegar = onNavegar) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                "Aparência",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = cores.primary
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = cores.surface),
                border = BorderStroke(1.dp, cores.outline)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .toggleable(
                            value = temaEscuro,
                            role = Role.Switch,
                            onValueChange = viewModel::definirTemaEscuro
                        )
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            "Tema escuro",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            "Alterna entre o tema claro e o escuro.",
                            style = MaterialTheme.typography.bodySmall,
                            color = cores.onSurfaceVariant
                        )
                    }
                    Switch(checked = temaEscuro, onCheckedChange = null)
                }
            }
        }
    }
}