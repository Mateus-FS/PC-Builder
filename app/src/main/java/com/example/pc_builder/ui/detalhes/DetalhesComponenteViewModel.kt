package com.example.pc_builder.ui.detalhes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.pc_builder.PcBuilderApp
import com.example.pc_builder.data.ComponenteRepository
import com.example.pc_builder.model.Componente
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class DetalhesComponenteViewModel(
    private val repository: ComponenteRepository,
    id: Long
) : ViewModel() {

    val componente: StateFlow<Componente?> =
        repository.observarPorId(id)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    fun excluir(componente: Componente, aoConcluir: () -> Unit) {
        viewModelScope.launch {
            repository.excluir(componente)
            aoConcluir()
        }
    }

    companion object {
        val Factory = viewModelFactory {
            initializer {
                val app = this[APPLICATION_KEY] as PcBuilderApp
                val id = checkNotNull(createSavedStateHandle().get<Long>("id"))
                DetalhesComponenteViewModel(app.componenteRepository, id)
            }
        }
    }
}