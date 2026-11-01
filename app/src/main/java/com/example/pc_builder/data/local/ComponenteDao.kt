package com.example.pc_builder.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ComponenteDao {

    @Insert
    suspend fun inserir(componente: ComponenteEntity): Long

    @Update
    suspend fun atualizar(componente: ComponenteEntity)

    @Delete
    suspend fun excluir(componente: ComponenteEntity)

    @Query("SELECT * FROM componentes WHERE id = :id")
    fun observarPorId(id: Long): Flow<ComponenteEntity?>

    // Busca + filtro de categoria na mesma consulta.
    // categoria = null  -> traz todas as categorias.
    @Query(
        """
        SELECT * FROM componentes
        WHERE (:categoria IS NULL OR categoria = :categoria)
          AND (nome LIKE '%' || :busca || '%'
               OR modelo LIKE '%' || :busca || '%'
               OR marca LIKE '%' || :busca || '%')
        ORDER BY nome COLLATE NOCASE ASC
        """
    )
    fun observar(busca: String, categoria: String?): Flow<List<ComponenteEntity>>
}