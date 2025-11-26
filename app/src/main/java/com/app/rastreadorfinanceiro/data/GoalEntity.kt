package com.app.rastreadorfinanceiro.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "goals")
data class GoalEntity(
        @PrimaryKey val id: String,
        val categoryId: String,
        val amount: Double
)