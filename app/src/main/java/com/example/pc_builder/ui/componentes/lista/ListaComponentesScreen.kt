package com.example.pc_builder.ui.componentes.lista

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pc_builder.model.CategoriaComponente
import com.example.pc_builder.model.Componente
import com.example.pc_builder.ui.navigation.BarraNavegacao
import com.example.pc_builder.ui.navigation.Rotas
import com.example.pc_builder.util.formatarPreco

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaComponentesScreen(
    onAbrir: (Long) -> Unit,
    onCadastrar: () -> Unit,
    onNavegar: (String) -> Unit,
    viewModel: ListaComponentesViewModel = viewModel(factory = ListaComponentesViewModel.Factory)
) {
    val busca by viewModel.busca.collectAsStateWithLifecycle()
    val categoria by viewModel.categoria.collectAsStateWithLifecycle()
    val componentes by viewModel.componentes.collectAsStateWithLifecycle()

    val cores = MaterialTheme.colorScheme
    val coresChip = FilterChipDefaults.filterChipColors(
        selectedContainerColor = cores.primaryContainer,
        selectedLabelColor = cores.onPrimaryContainer
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Catálogo", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = cores.background)
            )
        },
        bottomBar = { BarraNavegacao(rotaAtual = Rotas.LISTA, onNavegar = onNavegar) },
        floatingActionButton = {
            FloatingActionButton(onClick = onCadastrar) {
                Icon(Icons.Default.Add, contentDescription = "Cadastrar componente")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(horizontal = 16.dp)
        ) {
            OutlinedTextField(
                value = busca,
                onValueChange = viewModel::onBuscaChange,
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Buscar nome, modelo ou fabricante") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                singleLine = true
            )

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(vertical = 12.dp)
            ) {
                item {
                    FilterChip(
                        selected = categoria == null,
                        onClick = { viewModel.onCategoriaChange(null) },
                        label = { Text("Todas") },
                        colors = coresChip
                    )
                }
                items(CategoriaComponente.entries) { cat ->
                    FilterChip(
                        selected = categoria == cat,
                        onClick = { viewModel.onCategoriaChange(if (categoria == cat) null else cat) },
                        label = { Text(cat.rotuloCurto) },
                        colors = coresChip
                    )
                }
            }

            Text(
                "${componentes.size} componentes",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(Modifier.height(8.dp))

            if (componentes.isEmpty()) {
                Text(
                    "Nenhum componente encontrado.",
                    color = cores.onSurfaceVariant
                )
            }

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(bottom = 88.dp)
            ) {
                items(componentes, key = { it.id }) { componente ->
                    ComponenteItem(componente, onClick = { onAbrir(componente.id) })
                }
            }
        }
    }
}

@Composable
private fun ComponenteItem(componente: Componente, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    componente.nome,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    "${componente.marca} • ${componente.modelo}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    formatarPreco(componente.preco),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    componente.categoria.rotulo,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}