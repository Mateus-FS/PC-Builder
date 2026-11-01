package com.example.pc_builder.ui.catalogo

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pc_builder.data.local.ComponenteEntity
import com.example.pc_builder.model.CategoriaComponente
import com.example.pc_builder.util.precoParaCampo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormularioComponenteScreen(
    onVoltar: () -> Unit,
    viewModel: ComponenteViewModel = viewModel(factory = ComponenteViewModel.Factory)
) {
    val existente by viewModel.componente.collectAsStateWithLifecycle()

    var nome by rememberSaveable { mutableStateOf("") }
    var categoria by rememberSaveable { mutableStateOf(CategoriaComponente.PROCESSADOR) }
    var marca by rememberSaveable { mutableStateOf("") }
    var modelo by rememberSaveable { mutableStateOf("") }
    var preco by rememberSaveable { mutableStateOf("") }
    var loja by rememberSaveable { mutableStateOf("") }
    var descricao by rememberSaveable { mutableStateOf("") }
    var tentouSalvar by rememberSaveable { mutableStateOf(false) }
    var carregado by rememberSaveable { mutableStateOf(false) }

    // Modo edição: preenche os campos uma única vez com o que está no banco.
    LaunchedEffect(existente) {
        val c = existente
        if (c != null && !carregado) {
            nome = c.nome
            categoria = c.categoria
            marca = c.marca
            modelo = c.modelo
            preco = precoParaCampo(c.preco)
            loja = c.loja
            descricao = c.descricao.orEmpty()
            carregado = true
        }
    }

    val precoNumero = preco.replace(',', '.').toDoubleOrNull()
    val valido = nome.isNotBlank() && marca.isNotBlank() && modelo.isNotBlank() &&
            loja.isNotBlank() && precoNumero != null && precoNumero > 0

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (existente == null) "Cadastrar componente" else "Editar componente") },
                navigationIcon = {
                    IconButton(onClick = onVoltar) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .imePadding()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                "Identificação",
                style = MaterialTheme.typography.titleLarge
            )

            Campo(nome, { nome = it }, "Nome do componente *", tentouSalvar && nome.isBlank())

            SeletorCategoria(categoria) { categoria = it }

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Box(Modifier.weight(1f)) {
                    Campo(marca, { marca = it }, "Marca *", tentouSalvar && marca.isBlank())
                }
                Box(Modifier.weight(1f)) {
                    Campo(modelo, { modelo = it }, "Modelo *", tentouSalvar && modelo.isBlank())
                }
            }

            Text("Compra", style = MaterialTheme.typography.titleLarge)

            Campo(
                valor = preco,
                aoMudar = { preco = it },
                rotulo = "Preço (R$) *",
                erro = tentouSalvar && (precoNumero == null || precoNumero <= 0),
                teclado = KeyboardType.Decimal
            )
            Campo(loja, { loja = it }, "Loja *", tentouSalvar && loja.isBlank())
            Campo(
                valor = descricao,
                aoMudar = { descricao = it },
                rotulo = "Descrição (opcional)",
                erro = false,
                linhaUnica = false
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(onClick = onVoltar, modifier = Modifier.weight(1f)) {
                    Text("Cancelar")
                }
                Button(
                    modifier = Modifier.weight(1f),
                    onClick = {
                        tentouSalvar = true
                        if (valido) {
                            viewModel.salvar(
                                ComponenteEntity(
                                    id = existente?.id ?: 0,
                                    nome = nome.trim(),
                                    categoria = categoria,
                                    marca = marca.trim(),
                                    modelo = modelo.trim(),
                                    preco = precoNumero!!,
                                    loja = loja.trim(),
                                    descricao = descricao.trim().ifBlank { null }
                                ),
                                onVoltar
                            )
                        }
                    }
                ) { Text("Salvar peça") }
            }
        }
    }
}

@Composable
private fun Campo(
    valor: String,
    aoMudar: (String) -> Unit,
    rotulo: String,
    erro: Boolean,
    teclado: KeyboardType = KeyboardType.Text,
    linhaUnica: Boolean = true
) {
    OutlinedTextField(
        value = valor,
        onValueChange = aoMudar,
        label = { Text(rotulo) },
        isError = erro,
        supportingText = if (erro) ({ Text("Campo obrigatório ou inválido") }) else null,
        singleLine = linhaUnica,
        minLines = if (linhaUnica) 1 else 3,
        keyboardOptions = KeyboardOptions(keyboardType = teclado),
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
private fun SeletorCategoria(
    selecionada: CategoriaComponente,
    aoSelecionar: (CategoriaComponente) -> Unit
) {
    var aberto by rememberSaveable { mutableStateOf(false) }

    Box {
        OutlinedTextField(
            value = selecionada.rotulo,
            onValueChange = {},
            readOnly = true,
            label = { Text("Categoria *") },
            trailingIcon = { Icon(Icons.Default.ArrowDropDown, contentDescription = null) },
            modifier = Modifier.fillMaxWidth()
        )
        // Camada transparente que captura o clique (o TextField readOnly "engole" cliques).
        Box(
            Modifier
                .matchParentSize()
                .clickable { aberto = true }
        )
        DropdownMenu(expanded = aberto, onDismissRequest = { aberto = false }) {
            CategoriaComponente.entries.forEach { cat ->
                DropdownMenuItem(
                    text = { Text(cat.rotulo) },
                    onClick = {
                        aoSelecionar(cat)
                        aberto = false
                    }
                )
            }
        }
    }
}