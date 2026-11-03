package com.example.pc_builder.ui.montagens.lista

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import com.example.pc_builder.model.MontagemComComponentes
import com.example.pc_builder.ui.navigation.BarraNavegacao
import com.example.pc_builder.ui.navigation.Rotas
import com.example.pc_builder.util.formatarPreco

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaMontagensScreen(
    onAbrir: (Long) -> Unit,
    onCriar: () -> Unit,
    onNavegar: (String) -> Unit,
    viewModel: ListaMontagensViewModel = viewModel(factory = ListaMontagensViewModel.Factory)
) {
    val montagens by viewModel.montagens.collectAsStateWithLifecycle()
    val cores = MaterialTheme.colorScheme

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Montagens", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = cores.background)
            )
        },
        bottomBar = { BarraNavegacao(rotaAtual = Rotas.MONTAGENS, onNavegar = onNavegar) },
        floatingActionButton = {
            FloatingActionButton(onClick = onCriar) {
                Icon(Icons.Default.Add, contentDescription = "Nova montagem")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(horizontal = 16.dp)
        ) {
            if (montagens.isEmpty()) {
                Text(
                    "Nenhuma montagem salva ainda.",
                    color = cores.onSurfaceVariant,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(top = 8.dp, bottom = 88.dp)
            ) {
                items(montagens, key = { it.montagem.id }) { item ->
                    MontagemItem(item, onClick = { onAbrir(item.montagem.id) })
                }
            }
        }
    }
}

@Composable
private fun MontagemItem(item: MontagemComComponentes, onClick: () -> Unit) {
    val qtd = item.componentes.size
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
                    item.montagem.nome,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    if (qtd == 1) "1 componente" else "$qtd componentes",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Text(
                formatarPreco(item.total),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
}