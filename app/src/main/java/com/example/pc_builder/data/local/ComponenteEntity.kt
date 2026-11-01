package com.example.pc_builder.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.pc_builder.model.CategoriaComponente

@Entity(tableName = "componentes")
data class ComponenteEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val nome: String,
    val categoria: CategoriaComponente,
    val marca: String,
    val modelo: String,
    val preco: Double,
    val loja: String,
    val descricao: String? = null   // opcional
)