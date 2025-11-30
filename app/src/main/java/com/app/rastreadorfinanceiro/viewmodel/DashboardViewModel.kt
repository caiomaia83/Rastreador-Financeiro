package com.app.rastreadorfinanceiro.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.rastreadorfinanceiro.repository.CategoryRepository
import com.app.rastreadorfinanceiro.repository.TransactionRepository
import com.app.rastreadorfinanceiro.service.CategoryExpense
import com.app.rastreadorfinanceiro.service.DashboardService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DashboardViewModel(
    transactionRepo: TransactionRepository,
    categoryRepo: CategoryRepository
) : ViewModel() {

    private val service = DashboardService(transactionRepo, categoryRepo)

    private val _expensesByCategory = MutableStateFlow<List<CategoryExpense>>(emptyList())
    val expensesByCategory: StateFlow<List<CategoryExpense>> = _expensesByCategory.asStateFlow()

    init {
        loadDashboardData()
    }

    fun loadDashboardData() {
        viewModelScope.launch {
            _expensesByCategory.value = service.loadExpensesByCategory()
        }
    }
}