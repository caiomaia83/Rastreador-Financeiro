package com.app.rastreadorfinanceiro.model

import java.time.LocalDateTime

data class ExpenseModel(
    override val id: String,
    override val amount: Double,
    override val date: LocalDateTime,
    override val description: String,
    val category: CategoryModel
) : TransactionModel(id, amount, date, description)
