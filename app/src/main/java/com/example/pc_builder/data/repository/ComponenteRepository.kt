package com.example.pc_builder.data.repository

import com.example.pc_builder.data.local.ComponenteDao
import com.example.pc_builder.data.local.ComponenteEntity
import com.example.pc_builder.model.CategoriaComponente
import kotlinx.coroutines.flow.Flow

class ComponenteRepository(private val dao: ComponenteDao) {

    fun observar(busca: String, categoria: CategoriaComponente?): Flow<List<ComponenteEntity>> =
        dao.observar(busca, categoria?.name)

    fun observarPorId(id: Long): Flow<ComponenteEntity?> = dao.observarPorId(id)

    suspend fun inserir(componente: ComponenteEntity) = dao.inserir(componente)

    suspend fun atualizar(componente: ComponenteEntity) = dao.atualizar(componente)

    suspend fun excluir(componente: ComponenteEntity) = dao.excluir(componente)
}