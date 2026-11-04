package com.example.pc_builder.data.componentes

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.pc_builder.model.Componente
import kotlinx.coroutines.flow.Flow

@Dao
interface ComponenteDao {

    @Insert
    suspend fun inserir(componente: Componente): Long

    @Update
    suspend fun atualizar(componente: Componente)

    @Delete
    suspend fun excluir(componente: Componente)

    @Query("SELECT * FROM componentes WHERE id = :id")
    fun observarPorId(id: Long): Flow<Componente?>

    @Query(
        """
        SELECT * FROM componentes
        WHERE (:categoria IS NULL OR categoria = :categoria)
          AND (nome LIKE '%' || :busca || '%'
               OR modelo LIKE '%' || :busca || '%'
               OR marca LIKE '%' || :busca || '%')
        ORDER BY
            CASE WHEN :ordenacao = 'PRECO_MENOR' THEN preco END ASC,
            CASE WHEN :ordenacao = 'PRECO_MAIOR' THEN preco END DESC,
            nome COLLATE NOCASE ASC
        """
    )
    fun observar(
        busca: String,
        categoria: String?,
        ordenacao: String
    ): Flow<List<Componente>>
}