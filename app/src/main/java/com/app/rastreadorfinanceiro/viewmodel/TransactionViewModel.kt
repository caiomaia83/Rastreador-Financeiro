package com.app.rastreadorfinanceiro.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.app.rastreadorfinanceiro.model.CategoryModel
import com.app.rastreadorfinanceiro.model.ExpenseModel
import com.app.rastreadorfinanceiro.model.IncomeModel
import com.app.rastreadorfinanceiro.model.TransactionModel
import com.app.rastreadorfinanceiro.repository.TransactionRepository
import com.app.rastreadorfinanceiro.service.TransactionService
import java.time.LocalDateTime
import java.util.UUID

class TransactionViewModel(
    private val repo: TransactionRepository = TransactionRepository(),
    private val service: TransactionService = TransactionService(repo)
) : ViewModel() {

    private val _transactions = mutableStateListOf<TransactionModel>()
    val transactions: List<TransactionModel> get() = _transactions

    init {
        _transactions.clear()
        _transactions.addAll(repo.fetchTransactions())
    }

    fun refresh() {
        _transactions.clear()
        _transactions.addAll(repo.fetchTransactions())
    }

    fun addIncome(amount: Double, description: String, date: LocalDateTime = LocalDateTime.now()) {
        val safeAmount = if (amount < 0) kotlin.math.abs(amount) else amount
        val income = IncomeModel(UUID.randomUUID().toString(), safeAmount, date, description)
        repo.addIncome(income)
        _transactions.add(income)
    }

    fun addExpense(amount: Double, description: String, category: CategoryModel, date: LocalDateTime = LocalDateTime.now()) {
        val expense = ExpenseModel(UUID.randomUUID().toString(), amount, date, description, category)
        repo.addExpense(expense)
        _transactions.add(expense)
    }

    fun removeTransaction(transaction: TransactionModel) {
        repo.removeTransaction(transaction)
        _transactions.removeIf { it.id == transaction.id }
    }

    fun updateTransaction(transaction: TransactionModel) {
        repo.updateTransaction(transaction)
        val idx = _transactions.indexOfFirst { it.id == transaction.id }
        if (idx >= 0) _transactions[idx] = transaction
    }

    fun filterTransactions(start: LocalDateTime, end: LocalDateTime): List<TransactionModel> {
        return service.transactionsFilter(start, end)
    }

    fun sumExpenses(): Double = service.sumTotalExpenses(_transactions)
}
