package com.app.rastreadorfinanceiro.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface GoalDao {
    // SE O SEU ESTIVER SEM ": List<GoalEntity>", O ERRO ACONTECE!
    @Query("SELECT * FROM goals")
    suspend fun getAll(): List<GoalEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(goal: GoalEntity)

    @Delete
    suspend fun delete(goal: GoalEntity)
}