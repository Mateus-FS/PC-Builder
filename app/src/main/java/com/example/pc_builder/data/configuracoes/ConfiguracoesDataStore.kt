package com.example.pc_builder.data.configuracoes

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore

val Context.configuracoesDataStore: DataStore<Preferences> by preferencesDataStore(name = "configuracoes")