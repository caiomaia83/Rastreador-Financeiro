package com.app.rastreadorfinanceiro.service

import com.app.rastreadorfinanceiro.model.ExpenseModel
import com.app.rastreadorfinanceiro.model.IncomeModel
import com.app.rastreadorfinanceiro.model.TransactionModel
import com.app.rastreadorfinanceiro.repository.TransactionRepository
import java.time.LocalDateTime

class TransactionService(
    private val repo: TransactionRepository
) : ITransactionService {

    // Agora é suspend e chama o repo.fetchTransactions() que também é suspend
    override suspend fun transactionsFilter(start: LocalDateTime, end: LocalDateTime): List<TransactionModel> {
        return repo.fetchTransactions().filter { it.date >= start && it.date <= end }
    }

    override fun getIncomes(transactions: List<TransactionModel>): List<IncomeModel> {
        return transactions.filterIsInstance<IncomeModel>()
    }

    override fun getExpenses(transactions: List<TransactionModel>): List<ExpenseModel> {
        return transactions.filterIsInstance<ExpenseModel>()
    }

    override fun sumTotalExpenses(transactions: List<TransactionModel>): Double {
        return getExpenses(transactions).sumOf { it.amount }
    }
}