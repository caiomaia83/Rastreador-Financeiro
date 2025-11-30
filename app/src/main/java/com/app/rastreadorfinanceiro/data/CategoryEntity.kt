package com.app.rastreadorfinanceiro.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "categories")
data class CategoryEntity(
    @PrimaryKey val id: String,
    val name: String,
    val colorArgb: Int,
    val budgetLimit: Double? = null
)