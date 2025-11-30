package com.app.rastreadorfinanceiro.service

import com.app.rastreadorfinanceiro.model.ExpenseModel
import com.app.rastreadorfinanceiro.repository.CategoryRepository
import com.app.rastreadorfinanceiro.repository.TransactionRepository

class DashboardService(
    private val transactionRepo: TransactionRepository,
    private val categoryRepo: CategoryRepository
) : IDashboardService {

    override suspend fun loadExpensesByCategory(): List<CategoryExpense> {
        val transactions = transactionRepo.fetchTransactions()


        val expenses = transactions.filterIsInstance<ExpenseModel>()


        val grouped = expenses.groupBy { it.category.id }


        return grouped.map { (_, list) ->
            val total = list.sumOf { it.amount }

            val category = list.first().category

            CategoryExpense(
                name = category.name,
                value = total,
                color = category.color
            )
        }.sortedByDescending { it.value }
    }
}