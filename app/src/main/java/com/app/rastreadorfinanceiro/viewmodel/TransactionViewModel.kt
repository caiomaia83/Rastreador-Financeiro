package com.app.rastreadorfinanceiro.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.rastreadorfinanceiro.model.CategoryModel
import com.app.rastreadorfinanceiro.model.ExpenseModel
import com.app.rastreadorfinanceiro.model.IncomeModel
import com.app.rastreadorfinanceiro.model.TransactionModel
import com.app.rastreadorfinanceiro.repository.TransactionRepository
import com.app.rastreadorfinanceiro.service.TransactionService
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.util.UUID

class TransactionViewModel(
    private val repo: TransactionRepository
) : ViewModel() {

    private val service = TransactionService(repo)

    private val _transactions = mutableStateListOf<TransactionModel>()
    val transactions: List<TransactionModel> get() = _transactions

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            val fetched = repo.fetchTransactions()
            _transactions.clear()
            _transactions.addAll(fetched)
        }
    }

    fun addIncome(amount: Double, description: String, date: LocalDateTime = LocalDateTime.now()) {
        val safeAmount = if (amount < 0) kotlin.math.abs(amount) else amount
        val income = IncomeModel(UUID.randomUUID().toString(), safeAmount, date, description)

        viewModelScope.launch {
            repo.addIncome(income)
            refresh()
        }
    }

    fun addExpense(amount: Double, description: String, category: CategoryModel, date: LocalDateTime = LocalDateTime.now()) {
        val expense = ExpenseModel(UUID.randomUUID().toString(), amount, date, description, category)

        viewModelScope.launch {
            repo.addExpense(expense)
            refresh()
        }
    }

    fun removeTransaction(transaction: TransactionModel) {
        viewModelScope.launch {
            repo.removeTransaction(transaction)
            refresh()
        }
    }

    fun updateTransaction(transaction: TransactionModel) {
        viewModelScope.launch {
            repo.updateTransaction(transaction)
            refresh()
        }
    }


    fun filterTransactions(start: LocalDateTime, end: LocalDateTime) {
        viewModelScope.launch {
            val filteredList = service.transactionsFilter(start, end)
            _transactions.clear()
            _transactions.addAll(filteredList)
        }
    }


    fun sumExpenses(): Double = service.sumTotalExpenses(_transactions)
}