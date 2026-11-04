package com.example.pc_builder.data.configuracoes

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.pc_builder.model.Configuracoes
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class ConfiguracoesRepository(private val dataStore: DataStore<Preferences>) {

    private object Chaves {

        // Tema claro ou escuro
        val TEMA_ESCURO = booleanPreferencesKey("tema_escuro")

        // Orçamento padrão do usuário
        val ORCAMENTO_PADRAO = doublePreferencesKey("orcamento_padrao")

        // Preferência de ordenação dos componentes
        val ORDENACAO_PRECO = stringPreferencesKey("ordenacao_preco")
    }

    // Recupera todas as configurações armazenadas
    fun observar(): Flow<Configuracoes> = dataStore.data
        .catch { erro ->
            if (erro is IOException) {
                emit(emptyPreferences())
            } else {
                throw erro
            }
        }
        .map { prefs ->
            Configuracoes(
                temaEscuro = prefs[Chaves.TEMA_ESCURO],
                orcamentoPadrao = prefs[Chaves.ORCAMENTO_PADRAO] ?: 0.0,
                ordenacaoPreco = prefs[Chaves.ORDENACAO_PRECO] ?: "NOME"
            )
        }

    // Salva a preferência de tema
    suspend fun definirTemaEscuro(escuro: Boolean) {
        dataStore.edit { prefs ->
            prefs[Chaves.TEMA_ESCURO] = escuro
        }
    }

    // Salva o orçamento padrão
    suspend fun definirOrcamento(valor: Double) {
        dataStore.edit { prefs ->
            prefs[Chaves.ORCAMENTO_PADRAO] = valor
        }
    }

    // Salva a preferência de ordenação
    suspend fun definirOrdenacao(ordenacao: String) {
        dataStore.edit { prefs ->
            prefs[Chaves.ORDENACAO_PRECO] = ordenacao
        }
    }
}
