package com.example.pc_builder.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "montagens")
data class Montagem(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val nome: String
)