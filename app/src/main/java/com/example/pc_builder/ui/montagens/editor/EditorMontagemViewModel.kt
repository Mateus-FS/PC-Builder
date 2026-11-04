package com.example.pc_builder.ui.montagens.editor

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.pc_builder.PcBuilderApp
import com.example.pc_builder.data.componentes.ComponenteRepository
import com.example.pc_builder.data.configuracoes.ConfiguracoesRepository
import com.example.pc_builder.data.montagens.MontagemRepository
import com.example.pc_builder.model.Componente
import com.example.pc_builder.model.Montagem
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class EditorMontagemViewModel(
    private val montagemRepository: MontagemRepository,
    componenteRepository: ComponenteRepository,
    configuracoesRepository: ConfiguracoesRepository,
    private val id: Long?
) : ViewModel() {

    val editando: Boolean = id != null

    private val _nome = MutableStateFlow("")
    private val _idsSelecionados = MutableStateFlow<Set<Long>>(emptySet())

    val nome: StateFlow<String> = _nome.asStateFlow()

    val idsSelecionados: StateFlow<Set<Long>> =
        _idsSelecionados.asStateFlow()

    // Configurações salvas pelo usuário
    private val configuracoes = configuracoesRepository.observar()
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            null
        )

    // Orçamento padrão definido nas configurações
    val orcamentoPadrao: StateFlow<Double> =
        configuracoes
            .map { it?.orcamentoPadrao ?: 0.0 }
            .stateIn(
                viewModelScope,
                SharingStarted.Eagerly,
                0.0
            )

    // Catálogo de componentes respeitando a ordenação escolhida
    val catalogo: StateFlow<List<Componente>> =
        configuracoes
            .filterNotNull()
            .flatMapLatest { config ->
                componenteRepository.observar(
                    busca = "",
                    categoria = null,
                    ordenacao = config.ordenacaoPreco
                )
            }
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5_000),
                emptyList()
            )

    val selecionados: StateFlow<List<Componente>> =
        combine(catalogo, _idsSelecionados) { todos, ids ->
            todos.filter { it.id in ids }
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            emptyList()
        )

    // Soma dos preços das peças selecionadas
    val total: StateFlow<Double> =
        selecionados
            .map { lista -> lista.sumOf { it.preco } }
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5_000),
                0.0
            )

    init {
        if (id != null) {
            viewModelScope.launch {
                montagemRepository.observarPorId(id).first()?.let { m ->
                    _nome.value = m.montagem.nome
                    _idsSelecionados.value =
                        m.componentes.map { it.id }.toSet()
                }
            }
        }
    }

    fun onNomeChange(texto: String) {
        _nome.value = texto
    }

    fun alternar(componenteId: Long) {
        _idsSelecionados.value = _idsSelecionados.value.let {
            if (componenteId in it) {
                it - componenteId
            } else {
                it + componenteId
            }
        }
    }

    fun salvar(aoConcluir: () -> Unit) {
        val nome = _nome.value.trim()

        if (nome.isBlank()) return

        viewModelScope.launch {
            montagemRepository.salvar(
                Montagem(id = id ?: 0, nome = nome),
                _idsSelecionados.value.toList()
            )
            aoConcluir()
        }
    }

    fun excluir(aoConcluir: () -> Unit) {
        val idAtual = id ?: return

        viewModelScope.launch {
            montagemRepository.excluir(
                Montagem(id = idAtual, nome = _nome.value)
            )
            aoConcluir()
        }
    }

    companion object {
        val Factory = viewModelFactory {
            initializer {
                val app = this[APPLICATION_KEY] as PcBuilderApp

                val id = createSavedStateHandle()
                    .get<Long>("id")
                    ?.takeIf { it > 0 }

                EditorMontagemViewModel(
                    montagemRepository = app.montagemRepository,
                    componenteRepository = app.componenteRepository,
                    configuracoesRepository = app.configuracoesRepository,
                    id = id
                )
            }
        }
    }
}