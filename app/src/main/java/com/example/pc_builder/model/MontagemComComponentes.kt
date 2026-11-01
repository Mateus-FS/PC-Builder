package com.example.pc_builder.model

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class MontagemComComponentes(
    @Embedded val montagem: Montagem,
    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = MontagemComponente::class,
            parentColumn = "montagemId",
            entityColumn = "componenteId"
        )
    )
    val componentes: List<Componente>
) {
    val total: Double get() = componentes.sumOf { it.preco }
}