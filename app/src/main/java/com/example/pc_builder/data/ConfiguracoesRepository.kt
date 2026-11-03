package com.example.pc_builder.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.pc_builder.model.Configuracoes
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

// Cria o arquivo "configuracoes.preferences_pb" no armazenamento interno do app.
// Deve existir uma única instância por arquivo (por isso fica no nível do arquivo).
val Context.configuracoesDataStore: DataStore<Preferences> by preferencesDataStore(name = "configuracoes")

class ConfiguracoesRepository(private val dataStore: DataStore<Preferences>) {

    private object Chaves {
        val TEMA_ESCURO = booleanPreferencesKey("tema_escuro")
    }

    val configuracoes: Flow<Configuracoes> = dataStore.data
        .catch { erro ->
            // Se o arquivo falhar ao ler, usa os valores padrão em vez de derrubar o app.
            if (erro is IOException) emit(emptyPreferences()) else throw erro
        }
        .map { prefs -> Configuracoes(temaEscuro = prefs[Chaves.TEMA_ESCURO]) }

    suspend fun definirTemaEscuro(escuro: Boolean) {
        dataStore.edit { prefs -> prefs[Chaves.TEMA_ESCURO] = escuro }
    }
}