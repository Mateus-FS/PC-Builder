package com.example.pc_builder.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "componentes")
data class Componente(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val nome: String,
    val categoria: CategoriaComponente,
    val marca: String,
    val modelo: String,
    val preco: Double,
    val loja: String,
    val descricao: String? = null
)