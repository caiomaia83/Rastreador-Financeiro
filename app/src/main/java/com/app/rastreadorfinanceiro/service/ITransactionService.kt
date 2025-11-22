package com.app.rastreadorfinanceiro.service

import com.app.rastreadorfinanceiro.model.IncomeModel
import com.app.rastreadorfinanceiro.model.ExpenseModel
import com.app.rastreadorfinanceiro.model.TransactionModel
import java.time.LocalDateTime

interface ITransactionService {
    fun transactionsFilter(start: LocalDateTime, end: LocalDateTime): List<TransactionModel>
    fun getIncomes(transactions: List<TransactionModel>): List<IncomeModel>
    fun getExpenses(transactions: List<TransactionModel>): List<ExpenseModel>
    fun sumTotalExpenses(transactions: List<TransactionModel>): Double
}
