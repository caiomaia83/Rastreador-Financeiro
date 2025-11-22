package com.app.rastreadorfinanceiro.repository

import com.app.rastreadorfinanceiro.model.ExpenseModel
import com.app.rastreadorfinanceiro.model.IncomeModel
import com.app.rastreadorfinanceiro.model.TransactionModel

class TransactionRepository : BaseRepository<TransactionModel>() {
    private val storage: MutableList<TransactionModel> = mutableListOf()

    fun fetchTransactions(): List<TransactionModel> = storage.toList()

    fun addIncome(income: IncomeModel) {
        storage.add(income)
    }

    fun addExpense(expense: ExpenseModel) {
        storage.add(expense)
    }

    fun removeTransaction(transaction: TransactionModel) {
        storage.removeIf { it.id == transaction.id }
    }

    fun updateTransaction(transaction: TransactionModel) {
        val idx = storage.indexOfFirst { it.id == transaction.id }
        if (idx >= 0) storage[idx] = transaction
    }

    override fun save(data: List<TransactionModel>) {
        storage.clear()
        storage.addAll(data)
    }

    override fun load(): List<TransactionModel> = storage.toList()
}
