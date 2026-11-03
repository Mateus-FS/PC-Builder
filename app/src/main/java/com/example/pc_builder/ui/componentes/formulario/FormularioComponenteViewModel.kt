package com.example.pc_builder.ui.componentes.formulario

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.pc_builder.PcBuilderApp
import com.example.pc_builder.data.componentes.ComponenteRepository
import com.example.pc_builder.model.Componente
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class FormularioComponenteViewModel(
    private val repository: ComponenteRepository,
    id: Long?
) : ViewModel() {

    val componente: StateFlow<Componente?> =
        if (id != null) {
            repository.observarPorId(id)
                .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)
        } else {
            MutableStateFlow(null)
        }

    fun salvar(componente: Componente, aoConcluir: () -> Unit) {
        viewModelScope.launch {
            if (componente.id == 0L) repository.inserir(componente)
            else repository.atualizar(componente)
            aoConcluir()
        }
    }

    companion object {
        val Factory = viewModelFactory {
            initializer {
                val app = this[APPLICATION_KEY] as PcBuilderApp
                val id = createSavedStateHandle().get<Long>("id")?.takeIf { it > 0 }
                FormularioComponenteViewModel(app.componenteRepository, id)
            }
        }
    }
}