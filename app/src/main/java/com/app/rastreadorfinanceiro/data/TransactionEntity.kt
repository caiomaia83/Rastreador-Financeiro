package com.app.rastreadorfinanceiro.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey val id: String,
    val amount: Double,
    val description: String,
    val date: LocalDateTime,
    val type: String, // "INCOME" ou "EXPENSE"
    val categoryId: String? = null // Relacionamento com Categoria (apenas para despesa)
)