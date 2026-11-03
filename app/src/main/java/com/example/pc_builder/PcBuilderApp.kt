package com.example.pc_builder

import android.app.Application
import androidx.room.Room
import com.example.pc_builder.data.AppDatabase
import com.example.pc_builder.data.componentes.ComponenteRepository
import com.example.pc_builder.data.configuracoes.ConfiguracoesRepository
import com.example.pc_builder.data.montagens.MontagemRepository
import com.example.pc_builder.data.configuracoes.configuracoesDataStore

class PcBuilderApp : Application() {
    val database by lazy {
        Room.databaseBuilder(this, AppDatabase::class.java, "pcbuilder.db")
            .fallbackToDestructiveMigration(dropAllTables = true)
            .build()
    }
    val componenteRepository by lazy { ComponenteRepository(database.componenteDao()) }
    val montagemRepository by lazy { MontagemRepository(database.montagemDao()) }
    val configuracoesRepository by lazy { ConfiguracoesRepository(configuracoesDataStore) }
}