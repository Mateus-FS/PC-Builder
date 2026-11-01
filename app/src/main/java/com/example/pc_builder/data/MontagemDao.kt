package com.example.pc_builder.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.pc_builder.model.Montagem
import com.example.pc_builder.model.MontagemComComponentes
import com.example.pc_builder.model.MontagemComponente
import kotlinx.coroutines.flow.Flow

@Dao
interface MontagemDao {

    @Insert
    suspend fun inserir(montagem: Montagem): Long

    @Update
    suspend fun atualizar(montagem: Montagem)

    @Delete
    suspend fun excluir(montagem: Montagem)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun inserirLigacoes(ligacoes: List<MontagemComponente>)

    @Query("DELETE FROM montagem_componentes WHERE montagemId = :montagemId")
    suspend fun limparLigacoes(montagemId: Long)

    @Transaction
    @Query("SELECT * FROM montagens ORDER BY nome COLLATE NOCASE ASC")
    fun observarTodas(): Flow<List<MontagemComComponentes>>

    @Transaction
    @Query("SELECT * FROM montagens WHERE id = :id")
    fun observarPorId(id: Long): Flow<MontagemComComponentes?>

    @Transaction
    suspend fun salvar(montagem: Montagem, componenteIds: List<Long>): Long {
        val id = if (montagem.id == 0L) {
            inserir(montagem)
        } else {
            atualizar(montagem)
            montagem.id
        }
        limparLigacoes(id)
        inserirLigacoes(componenteIds.map { MontagemComponente(id, it) })
        return id
    }
}