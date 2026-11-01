package com.example.pc_builder.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "montagem_componentes",
    primaryKeys = ["montagemId", "componenteId"],
    foreignKeys = [
        ForeignKey(
            entity = Montagem::class,
            parentColumns = ["id"],
            childColumns = ["montagemId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Componente::class,
            parentColumns = ["id"],
            childColumns = ["componenteId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("componenteId")]
)
data class MontagemComponente(
    val montagemId: Long,
    val componenteId: Long
)