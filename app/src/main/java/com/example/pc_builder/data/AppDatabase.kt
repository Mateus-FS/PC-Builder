package com.example.pc_builder.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.pc_builder.model.Componente

@Database(entities = [Componente::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun componenteDao(): ComponenteDao
}