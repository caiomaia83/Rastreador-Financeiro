package com.app.rastreadorfinanceiro.model

import java.time.LocalDateTime

abstract class TransactionModel(
    open val id: String,
    open val amount: Double,
    open val date: LocalDateTime,
    open val description: String
)
