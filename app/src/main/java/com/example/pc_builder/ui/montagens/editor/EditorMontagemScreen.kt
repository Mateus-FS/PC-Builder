
package com.example.pc_builder.ui.montagens.editor

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pc_builder.model.Componente
import com.example.pc_builder.util.formatarPreco

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditorMontagemScreen(
    onVoltar: () -> Unit,
    viewModel: EditorMontagemViewModel = viewModel(factory = EditorMontagemViewModel.Factory)
) {
    val nome by viewModel.nome.collectAsStateWithLifecycle()
    val catalogo by viewModel.catalogo.collectAsStateWithLifecycle()
    val ids by viewModel.idsSelecionados.collectAsStateWithLifecycle()
    val selecionados by viewModel.selecionados.collectAsStateWithLifecycle()
    val total by viewModel.total.collectAsStateWithLifecycle()
    val orcamentoPadrao by viewModel.orcamentoPadrao.collectAsStateWithLifecycle()

    val orcamentoRestante = orcamentoPadrao - total

    var tentouSalvar by rememberSaveable { mutableStateOf(false) }
    var escolhendo by rememberSaveable { mutableStateOf(false) }
    var confirmandoExclusao by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        if (viewModel.editando) "Editar montagem"
                        else "Nova montagem"
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onVoltar) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar"
                        )
                    }
                },
                actions = {
                    if (viewModel.editando) {
                        IconButton(
                            onClick = { confirmandoExclusao = true }
                        ) {
                            Icon(
                                Icons.Default.Delete,
                                contentDescription = "Excluir montagem"
                            )
                        }
                    }
                }
            )
        },
        bottomBar = {
            Surface(tonalElevation = 3.dp) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            "Total",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            formatarPreco(total),
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Button(
                        onClick = {
                            tentouSalvar = true
                            if (nome.isNotBlank()) {
                                viewModel.salvar(onVoltar)
                            }
                        }
                    ) {
                        Text("Salvar montagem")
                    }
                }
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {

            // Nome da montagem
            item {
                val erro = tentouSalvar && nome.isBlank()

                OutlinedTextField(
                    value = nome,
                    onValueChange = viewModel::onNomeChange,
                    label = { Text("Nome da montagem *") },
                    isError = erro,
                    supportingText = if (erro) {
                        { Text("Campo obrigatório") }
                    } else {
                        null
                    },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // Orçamento padrão
            if (orcamentoPadrao > 0.0) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                "Orçamento padrão",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    "Limite definido",
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    formatarPreco(orcamentoPadrao),
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    "Total da montagem",
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    formatarPreco(total),
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    if (orcamentoRestante >= 0.0) {
                                        "Orçamento restante"
                                    } else {
                                        "Valor excedido"
                                    },
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )

                                Text(
                                    formatarPreco(kotlin.math.abs(orcamentoRestante)),
                                    fontWeight = FontWeight.Bold,
                                    color = if (orcamentoRestante >= 0.0) {
                                        MaterialTheme.colorScheme.primary
                                    } else {
                                        MaterialTheme.colorScheme.error
                                    }
                                )
                            }
                        }
                    }
                }
            }

            // Componentes selecionados
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Componentes (${selecionados.size})",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.weight(1f)
                    )

                    OutlinedButton(onClick = { escolhendo = true }) {
                        Icon(Icons.Default.Add, contentDescription = null)
                        Text(" Adicionar")
                    }
                }
            }

            if (selecionados.isEmpty()) {
                item {
                    Text(
                        "Nenhum componente selecionado.",
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            items(selecionados, key = { it.id }) { c ->
                ItemSelecionado(
                    c,
                    onRemover = { viewModel.alternar(c.id) }
                )
            }
        }
    }

    // Diálogo de seleção de componentes
    if (escolhendo) {
        AlertDialog(
            onDismissRequest = { escolhendo = false },
            title = { Text("Selecionar componentes") },
            text = {
                if (catalogo.isEmpty()) {
                    Text("Cadastre componentes no catálogo primeiro.")
                } else {
                    LazyColumn {
                        items(catalogo, key = { it.id }) { c ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        viewModel.alternar(c.id)
                                    },
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Checkbox(
                                    checked = c.id in ids,
                                    onCheckedChange = {
                                        viewModel.alternar(c.id)
                                    }
                                )

                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        c.nome,
                                        style = MaterialTheme.typography.bodyLarge
                                    )
                                    Text(
                                        "${c.categoria.rotuloCurto} • ${formatarPreco(c.preco)}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { escolhendo = false }) {
                    Text("Concluir")
                }
            }
        )
    }

    // Confirmação de exclusão
    if (confirmandoExclusao) {
        AlertDialog(
            onDismissRequest = { confirmandoExclusao = false },
            title = { Text("Excluir montagem?") },
            text = {
                Text("“$nome” será removida. Os componentes continuam no catálogo.")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        confirmandoExclusao = false
                        viewModel.excluir(onVoltar)
                    }
                ) {
                    Text("Excluir")
                }
            },
            dismissButton = {
                TextButton(onClick = { confirmandoExclusao = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@Composable
private fun ItemSelecionado(
    componente: Componente,
    onRemover: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, top = 8.dp, bottom = 8.dp, end = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    componente.nome,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    "${componente.categoria.rotuloCurto} • ${componente.marca}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Text(
                formatarPreco(componente.preco),
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )

            IconButton(onClick = onRemover) {
                Icon(
                    Icons.Default.Close,
                    contentDescription = "Remover da montagem"
                )
            }
        }
    }
}