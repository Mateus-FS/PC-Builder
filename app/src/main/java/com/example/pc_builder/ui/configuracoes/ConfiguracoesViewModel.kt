package com.example.pc_builder.ui.configuracoes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.pc_builder.PcBuilderApp
import com.example.pc_builder.data.ConfiguracoesRepository
import com.example.pc_builder.model.Configuracoes
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ConfiguracoesViewModel(
    private val repository: ConfiguracoesRepository
) : ViewModel() {

    // null = ainda lendo do disco (poucos milissegundos na abertura do app).
    val configuracoes: StateFlow<Configuracoes?> =
        repository.configuracoes
            .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    fun definirTemaEscuro(escuro: Boolean) {
        viewModelScope.launch { repository.definirTemaEscuro(escuro) }
    }

    companion object {
        val Factory = viewModelFactory {
            initializer {
                val app = this[APPLICATION_KEY] as PcBuilderApp
                ConfiguracoesViewModel(app.configuracoesRepository)
            }
        }
    }
}