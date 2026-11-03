package com.example.pc_builder.data.componentes

import com.example.pc_builder.model.CategoriaComponente
import com.example.pc_builder.model.Componente
import kotlinx.coroutines.flow.Flow

class ComponenteRepository(private val dao: ComponenteDao) {

    fun observar(busca: String, categoria: CategoriaComponente?): Flow<List<Componente>> =
        dao.observar(busca, categoria?.name)

    fun observarPorId(id: Long): Flow<Componente?> = dao.observarPorId(id)

    suspend fun inserir(componente: Componente) = dao.inserir(componente)

    suspend fun atualizar(componente: Componente) = dao.atualizar(componente)

    suspend fun excluir(componente: Componente) = dao.excluir(componente)
}