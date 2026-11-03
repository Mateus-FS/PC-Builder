package com.example.pc_builder.ui.componentes.lista

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.pc_builder.PcBuilderApp
import com.example.pc_builder.data.componentes.ComponenteRepository
import com.example.pc_builder.model.CategoriaComponente
import com.example.pc_builder.model.Componente
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn

@OptIn(ExperimentalCoroutinesApi::class)
class ListaComponentesViewModel(repository: ComponenteRepository) : ViewModel() {

    private val _busca = MutableStateFlow("")
    private val _categoria = MutableStateFlow<CategoriaComponente?>(null)

    val busca: StateFlow<String> = _busca.asStateFlow()
    val categoria: StateFlow<CategoriaComponente?> = _categoria.asStateFlow()
    val componentes: StateFlow<List<Componente>> =
        combine(_busca, _categoria) { busca, categoria -> busca.trim() to categoria }
            .flatMapLatest { (busca, categoria) -> repository.observar(busca, categoria) }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun onBuscaChange(texto: String) { _busca.value = texto }
    fun onCategoriaChange(categoria: CategoriaComponente?) { _categoria.value = categoria }

    companion object {
        val Factory = viewModelFactory {
            initializer {
                val app =
                    this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as PcBuilderApp
                ListaComponentesViewModel(app.componenteRepository)
            }
        }
    }
}