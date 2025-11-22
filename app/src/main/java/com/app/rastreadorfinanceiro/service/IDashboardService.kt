package com.app.rastreadorfinanceiro.service

import com.app.rastreadorfinanceiro.model.ExpenseModel
import com.app.rastreadorfinanceiro.model.CategoryModel

interface IDashboardService {
    fun expensesByCategory(expenses: List<ExpenseModel>, categories: List<CategoryModel>): Map<String, Double>
    fun filterExpensesByCategory(expenses: List<ExpenseModel>, category: CategoryModel): List<ExpenseModel>
}
