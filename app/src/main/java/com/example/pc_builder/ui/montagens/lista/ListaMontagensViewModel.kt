package com.example.pc_builder.ui.montagens.lista

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.pc_builder.PcBuilderApp
import com.example.pc_builder.data.montagens.MontagemRepository
import com.example.pc_builder.model.MontagemComComponentes
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class ListaMontagensViewModel(repository: MontagemRepository) : ViewModel() {

    val montagens: StateFlow<List<MontagemComComponentes>> =
        repository.observarTodas()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    companion object {
        val Factory = viewModelFactory {
            initializer {
                val app = this[APPLICATION_KEY] as PcBuilderApp
                ListaMontagensViewModel(app.montagemRepository)
            }
        }
    }
}