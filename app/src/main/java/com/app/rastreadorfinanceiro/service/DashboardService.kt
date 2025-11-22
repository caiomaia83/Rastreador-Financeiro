package com.app.rastreadorfinanceiro.service

import com.app.rastreadorfinanceiro.model.CategoryModel
import com.app.rastreadorfinanceiro.model.ExpenseModel

class DashboardService : IDashboardService {
    override fun expensesByCategory(expenses: List<ExpenseModel>, categories: List<CategoryModel>): Map<String, Double> {
        return expenses.groupBy { it.category.id }.mapValues { entry -> entry.value.sumOf { it.amount } }
    }

    override fun filterExpensesByCategory(expenses: List<ExpenseModel>, category: CategoryModel): List<ExpenseModel> {
        return expenses.filter { it.category.id == category.id }
    }
}
