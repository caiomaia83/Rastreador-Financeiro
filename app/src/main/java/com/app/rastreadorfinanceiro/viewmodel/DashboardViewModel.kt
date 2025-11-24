package com.app.rastreadorfinanceiro.viewmodel

import androidx.lifecycle.ViewModel
import com.app.rastreadorfinanceiro.model.CategoryModel
import com.app.rastreadorfinanceiro.model.ExpenseModel
import com.app.rastreadorfinanceiro.service.DashboardService

class DashboardViewModel(
    private val service: DashboardService = DashboardService()
) : ViewModel() {

    fun expensesByCategory(expenses: List<ExpenseModel>, categories: List<CategoryModel>): Map<String, Double> {
        return service.expensesByCategory(expenses, categories)
    }

    fun filterExpensesByCategory(expenses: List<ExpenseModel>, category: CategoryModel): List<ExpenseModel> {
        return service.filterExpensesByCategory(expenses, category)
    }
}
