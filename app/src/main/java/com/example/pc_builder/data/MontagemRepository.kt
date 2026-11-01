package com.example.pc_builder.data

import com.example.pc_builder.model.Montagem
import com.example.pc_builder.model.MontagemComComponentes
import kotlinx.coroutines.flow.Flow

class MontagemRepository(private val dao: MontagemDao) {

    fun observarTodas(): Flow<List<MontagemComComponentes>> = dao.observarTodas()

    fun observarPorId(id: Long): Flow<MontagemComComponentes?> = dao.observarPorId(id)

    suspend fun salvar(montagem: Montagem, componenteIds: List<Long>): Long =
        dao.salvar(montagem, componenteIds)

    suspend fun excluir(montagem: Montagem) = dao.excluir(montagem)
}