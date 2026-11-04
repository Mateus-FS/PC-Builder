package com.example.pc_builder.ui.configuracoes

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pc_builder.ui.navigation.BarraNavegacao
import com.example.pc_builder.ui.navigation.Rotas
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfiguracoesScreen(
    onNavegar: (String) -> Unit,
    viewModel: ConfiguracoesViewModel = viewModel(factory = ConfiguracoesViewModel.Factory)
) {
    val configuracoes by viewModel.configuracoes.collectAsStateWithLifecycle()
    val temaEscuro = configuracoes?.temaEscuro ?: isSystemInDarkTheme()
    val cores = MaterialTheme.colorScheme

    var orcamentoTexto by remember(configuracoes?.orcamentoPadrao) {
        mutableStateOf(
            if ((configuracoes?.orcamentoPadrao ?: 0.0) > 0.0) {
                configuracoes?.orcamentoPadrao.toString()
            } else {
                ""
            }
        )
    }

    var menuOrdenacaoAberto by remember { mutableStateOf(false) }

    val ordenacaoAtual = configuracoes?.ordenacaoPreco ?: "NOME"

    val opcoesOrdenacao = listOf(
        "NOME" to "Ordem alfabética",
        "PRECO_MENOR" to "Menor preço primeiro",
        "PRECO_MAIOR" to "Maior preço primeiro"
    )

    val nomeOrdenacao = opcoesOrdenacao
        .firstOrNull { it.first == ordenacaoAtual }
        ?.second ?: "Ordem alfabética"

    val formatoMoeda = NumberFormat.getCurrencyInstance(
        Locale("pt", "BR")
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Configurações", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = cores.background
                )
            )
        },
        bottomBar = {
            BarraNavegacao(
                rotaAtual = Rotas.CONFIGURACOES,
                onNavegar = onNavegar
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            // APARÊNCIA
            Text(
                "Aparência",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = cores.primary
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = cores.surface
                ),
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

                    Switch(
                        checked = temaEscuro,
                        onCheckedChange = null
                    )
                }
            }

            // ORÇAMENTO PADRÃO
            Text(
                "Orçamento padrão",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = cores.primary
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = cores.surface
                ),
                border = BorderStroke(1.dp, cores.outline)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        "Defina seu orçamento",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )

                    Text(
                        "Esse valor será utilizado como referência ao criar uma nova montagem.",
                        style = MaterialTheme.typography.bodySmall,
                        color = cores.onSurfaceVariant
                    )

                    OutlinedTextField(
                        value = orcamentoTexto,
                        onValueChange = { novoValor ->
                            if (novoValor.matches(Regex("^\\d*([.,]\\d{0,2})?$"))) {
                                orcamentoTexto = novoValor
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Valor em reais (R$)") },
                        placeholder = { Text("Ex.: 5000,00") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Decimal
                        )
                    )

                    Button(
                        onClick = {
                            val valor = orcamentoTexto
                                .replace(",", ".")
                                .toDoubleOrNull()

                            if (valor != null && valor >= 0.0) {
                                viewModel.definirOrcamento(valor)
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = orcamentoTexto.isNotBlank()
                    ) {
                        Text("Salvar orçamento")
                    }

                    if ((configuracoes?.orcamentoPadrao ?: 0.0) > 0.0) {
                        Text(
                            "Orçamento atual: ${
                                formatoMoeda.format(configuracoes?.orcamentoPadrao)
                            }",
                            style = MaterialTheme.typography.bodyMedium,
                            color = cores.primary,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            // ORDENAÇÃO DO CATÁLOGO
            Text(
                "Ordenação do catálogo",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = cores.primary
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = cores.surface
                ),
                border = BorderStroke(1.dp, cores.outline)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        "Organizar componentes",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )

                    Text(
                        "Escolha como os componentes serão apresentados no catálogo.",
                        style = MaterialTheme.typography.bodySmall,
                        color = cores.onSurfaceVariant
                    )

                    androidx.compose.foundation.layout.Box {
                        androidx.compose.material3.OutlinedButton(
                            onClick = {
                                menuOrdenacaoAberto = true
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(nomeOrdenacao)
                        }

                        DropdownMenu(
                            expanded = menuOrdenacaoAberto,
                            onDismissRequest = {
                                menuOrdenacaoAberto = false
                            }
                        ) {
                            opcoesOrdenacao.forEach { (codigo, nome) ->
                                DropdownMenuItem(
                                    text = { Text(nome) },
                                    onClick = {
                                        viewModel.definirOrdenacao(codigo)
                                        menuOrdenacaoAberto = false
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}