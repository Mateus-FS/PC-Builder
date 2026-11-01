package com.example.pc_builder.ui.catalogo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.pc_builder.PcBuilderApp
import com.example.pc_builder.data.local.ComponenteEntity
import com.example.pc_builder.data.repository.ComponenteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

// Usado nas telas de Detalhe e de Formulário (cadastrar/editar).
// id == null  -> cadastro de um novo componente.
class ComponenteViewModel(
    private val repository: ComponenteRepository,
    id: Long?
) : ViewModel() {

    val componente: StateFlow<ComponenteEntity?> =
        if (id != null) {
            repository.observarPorId(id)
                .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)
        } else {
            MutableStateFlow(null)
        }

    fun salvar(componente: ComponenteEntity, aoConcluir: () -> Unit) {
        viewModelScope.launch {
            if (componente.id == 0L) repository.inserir(componente)
            else repository.atualizar(componente)
            aoConcluir()
        }
    }

    fun excluir(componente: ComponenteEntity, aoConcluir: () -> Unit) {
        viewModelScope.launch {
            repository.excluir(componente)
            aoConcluir()
        }
    }

    companion object {
        val Factory = viewModelFactory {
            initializer {
                val app = this[APPLICATION_KEY] as PcBuilderApp
                // O argumento "id" da rota de navegação chega aqui.
                val id = createSavedStateHandle().get<Long>("id")?.takeIf { it > 0 }
                ComponenteViewModel(app.componenteRepository, id)
            }
        }
    }
}