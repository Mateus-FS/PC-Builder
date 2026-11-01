package com.example.pc_builder

import android.app.Application
import androidx.room.Room
import com.example.pc_builder.data.AppDatabase
import com.example.pc_builder.data.ComponenteRepository

class PcBuilderApp : Application() {
    val database by lazy {
        Room.databaseBuilder(this, AppDatabase::class.java, "pcbuilder.db").build()
    }
    val componenteRepository by lazy { ComponenteRepository(database.componenteDao()) }
}