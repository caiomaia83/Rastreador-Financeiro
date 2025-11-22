package com.app.rastreadorfinanceiro.model

import java.time.LocalDateTime

data class IncomeModel(
    override val id: String,
    override val amount: Double,
    override val date: LocalDateTime,
    override val description: String
) : TransactionModel(id, amount, date, description)
