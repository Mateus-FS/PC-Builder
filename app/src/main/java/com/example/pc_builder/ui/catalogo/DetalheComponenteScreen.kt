package com.example.pc_builder.ui.catalogo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pc_builder.util.formatarPreco

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalheComponenteScreen(
    onVoltar: () -> Unit,
    onEditar: (Long) -> Unit,
    viewModel: ComponenteViewModel = viewModel(factory = ComponenteViewModel.Factory)
) {
    val componente by viewModel.componente.collectAsStateWithLifecycle()
    var confirmandoExclusao by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalhes do componente") },
                navigationIcon = {
                    IconButton(onClick = onVoltar) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                }
            )
        }
    ) { padding ->
        componente?.let { c ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(c.nome, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                Text(c.categoria.rotulo, color = MaterialTheme.colorScheme.primary)
                Text(
                    formatarPreco(c.preco),
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
                HorizontalDivider()
                LinhaDetalhe("Marca", c.marca)
                LinhaDetalhe("Modelo", c.modelo)
                LinhaDetalhe("Loja", c.loja)
                LinhaDetalhe("Descrição", c.descricao?.takeIf { it.isNotBlank() } ?: "—")

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = { confirmandoExclusao = true },
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(Icons.Default.Delete, contentDescription = null)
                        Text(" Excluir")
                    }
                    Button(
                        onClick = { onEditar(c.id) },
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(Icons.Default.Edit, contentDescription = null)
                        Text(" Editar")
                    }
                }
            }

            if (confirmandoExclusao) {
                AlertDialog(
                    onDismissRequest = { confirmandoExclusao = false },
                    title = { Text("Excluir componente?") },
                    text = { Text("“${c.nome}” será removido do catálogo.") },
                    confirmButton = {
                        TextButton(onClick = {
                            confirmandoExclusao = false
                            viewModel.excluir(c, onVoltar)
                        }) { Text("Excluir") }
                    },
                    dismissButton = {
                        TextButton(onClick = { confirmandoExclusao = false }) { Text("Cancelar") }
                    }
                )
            }
        }
    }
}

@Composable
private fun LinhaDetalhe(rotulo: String, valor: String) {
    Column {
        Text(
            rotulo,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(valor, style = MaterialTheme.typography.bodyLarge)
    }
}