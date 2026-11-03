package com.example.pc_builder

import android.app.Application
import androidx.room.Room
import com.example.pc_builder.data.AppDatabase
import com.example.pc_builder.data.ComponenteRepository
import com.example.pc_builder.data.ConfiguracoesRepository
import com.example.pc_builder.data.MontagemRepository
import com.example.pc_builder.data.configuracoesDataStore

class PcBuilderApp : Application() {
    val database by lazy {
        Room.databaseBuilder(this, AppDatabase::class.java, "pcbuilder.db")
            .fallbackToDestructiveMigration(false)
            .build()
    }
    val componenteRepository by lazy { ComponenteRepository(database.componenteDao()) }
    val montagemRepository by lazy { MontagemRepository(database.montagemDao()) }
    val configuracoesRepository by lazy { ConfiguracoesRepository(configuracoesDataStore) }
}