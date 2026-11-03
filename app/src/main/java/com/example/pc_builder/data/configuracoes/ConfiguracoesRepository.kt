package com.example.pc_builder.data.configuracoes

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import com.example.pc_builder.model.Configuracoes
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class ConfiguracoesRepository(private val dataStore: DataStore<Preferences>) {

    private object Chaves {
        val TEMA_ESCURO = booleanPreferencesKey("tema_escuro")
    }

    fun observar(): Flow<Configuracoes> = dataStore.data
        .catch { erro ->
            if (erro is IOException) emit(emptyPreferences()) else throw erro
        }
        .map { prefs -> Configuracoes(temaEscuro = prefs[Chaves.TEMA_ESCURO]) }

    suspend fun definirTemaEscuro(escuro: Boolean) {
        dataStore.edit { prefs -> prefs[Chaves.TEMA_ESCURO] = escuro }
    }
}