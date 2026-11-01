package com.example.pc_builder.ui.componentes.formulario

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pc_builder.model.CategoriaComponente
import com.example.pc_builder.model.Componente
import com.example.pc_builder.util.precoParaCampo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormularioComponenteScreen(
    onVoltar: () -> Unit,
    viewModel: FormularioComponenteViewModel = viewModel(factory = FormularioComponenteViewModel.Factory)
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
    val precoValido = precoNumero != null && precoNumero > 0
    val valido = nome.isNotBlank() && marca.isNotBlank() && modelo.isNotBlank() &&
            loja.isNotBlank() && precoValido

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

            Campo(
                valor = nome,
                aoMudar = { nome = it },
                rotulo = "Nome do componente *",
                erro = tentouSalvar && nome.isBlank(),
                capitalization = KeyboardCapitalization.Words
            )

            SeletorCategoria(
                selecionada = categoria,
                aoSelecionar = { categoria = it }
            )

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Campo(
                    valor = marca,
                    aoMudar = { marca = it },
                    rotulo = "Marca *",
                    erro = tentouSalvar && marca.isBlank(),
                    capitalization = KeyboardCapitalization.Words,
                    modifier = Modifier.weight(1f)
                )
                Campo(
                    valor = modelo,
                    aoMudar = { modelo = it },
                    rotulo = "Modelo *",
                    erro = tentouSalvar && modelo.isBlank(),
                    capitalization = KeyboardCapitalization.Words,
                    modifier = Modifier.weight(1f)
                )
            }

            Text("Compra", style = MaterialTheme.typography.titleLarge)

            Campo(
                valor = preco,
                aoMudar = { preco = it },
                rotulo = "Preço (R$) *",
                erro = tentouSalvar && !precoValido,
                mensagemErro = if (preco.isNotBlank() && precoNumero == null) "Preço inválido" else "Informe um preço maior que zero",
                teclado = KeyboardType.Decimal
            )

            Campo(
                valor = loja,
                aoMudar = { loja = it },
                rotulo = "Loja *",
                erro = tentouSalvar && loja.isBlank(),
                capitalization = KeyboardCapitalization.Words
            )

            Campo(
                valor = descricao,
                aoMudar = { descricao = it },
                rotulo = "Descrição (opcional)",
                erro = false,
                linhaUnica = false,
                imeAction = ImeAction.Done
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = onVoltar,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Cancelar")
                }
                Button(
                    modifier = Modifier.weight(1f),
                    onClick = {
                        tentouSalvar = true
                        if (valido) {
                            viewModel.salvar(
                                Componente(
                                    id = existente?.id ?: 0,
                                    nome = nome.trim(),
                                    categoria = categoria,
                                    marca = marca.trim(),
                                    modelo = modelo.trim(),
                                    preco = precoNumero,
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
    modifier: Modifier = Modifier,
    mensagemErro: String = "Campo obrigatório ou inválido",
    teclado: KeyboardType = KeyboardType.Text,
    capitalization: KeyboardCapitalization = KeyboardCapitalization.None,
    imeAction: ImeAction = ImeAction.Next,
    linhaUnica: Boolean = true
) {
    OutlinedTextField(
        value = valor,
        onValueChange = aoMudar,
        label = { Text(rotulo) },
        isError = erro,
        supportingText = if (erro) { { Text(mensagemErro) } } else null,
        singleLine = linhaUnica,
        minLines = if (linhaUnica) 1 else 3,
        keyboardOptions = KeyboardOptions(
            keyboardType = teclado,
            capitalization = capitalization,
            imeAction = imeAction
        ),
        modifier = modifier.fillMaxWidth()
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SeletorCategoria(
    selecionada: CategoriaComponente,
    aoSelecionar: (CategoriaComponente) -> Unit
) {
    var aberto by rememberSaveable { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = aberto,
        onExpandedChange = { aberto = !aberto }
    ) {
        OutlinedTextField(
            value = selecionada.rotulo,
            onValueChange = {},
            readOnly = true,
            label = { Text("Categoria *") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = aberto) },
            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
            modifier = Modifier
                .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
                .fillMaxWidth()
        )
        ExposedDropdownMenu(
            expanded = aberto,
            onDismissRequest = { aberto = false }
        ) {
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