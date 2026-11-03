package com.example.pc_builder.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.pc_builder.data.componentes.ComponenteDao
import com.example.pc_builder.data.montagens.MontagemDao
import com.example.pc_builder.model.Componente
import com.example.pc_builder.model.Montagem
import com.example.pc_builder.model.MontagemComponente

@Database(
    entities = [Componente::class, Montagem::class, MontagemComponente::class],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun componenteDao(): ComponenteDao
    abstract fun montagemDao(): MontagemDao
}