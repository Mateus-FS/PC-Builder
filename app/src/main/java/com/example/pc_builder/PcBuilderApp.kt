package com.example.pc_builder

import android.app.Application
import com.example.pc_builder.data.local.AppDatabase
import com.example.pc_builder.data.repository.ComponenteRepository

class PcBuilderApp : Application() {
    val database by lazy { AppDatabase.getInstance(this) }
    val componenteRepository by lazy { ComponenteRepository(database.componenteDao()) }
}