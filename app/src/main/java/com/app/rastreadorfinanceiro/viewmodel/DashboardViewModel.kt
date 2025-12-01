package com.app.rastreadorfinanceiro.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.rastreadorfinanceiro.model.ExpenseModel
import com.app.rastreadorfinanceiro.model.TransactionModel
import com.app.rastreadorfinanceiro.repository.CategoryRepository
import com.app.rastreadorfinanceiro.repository.TransactionRepository
import com.app.rastreadorfinanceiro.service.DashboardService
import com.app.rastreadorfinanceiro.service.TransactionService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import java.time.LocalDateTime



class DashboardViewModel(
    transactionRepo: TransactionRepository,
    categoryRepo: CategoryRepository
) : ViewModel() {

    private val serviceT = TransactionService(transactionRepo)

    private val _transactions = mutableStateListOf<ExpenseModel>()
    val transactions: List<ExpenseModel> get() = _transactions

    private val service = DashboardService(transactionRepo, categoryRepo)

    private val _expensesByCategory = MutableStateFlow<Map<String, Double>>(emptyMap())
    val expensesByCategory: StateFlow<Map<String, Double>> = _expensesByCategory.asStateFlow()

    init {
        loadDashboardData()
    }

    fun loadDashboardData() {
        viewModelScope.launch {
            // O ViewModel ficou bem mais limpo! O Service faz o trabalho pesado.
            _expensesByCategory.value = service.loadExpensesByCategory()
            filterTransactions(LocalDateTime.now())
        }
    }
    fun filterTransactions(now: LocalDateTime) {

        viewModelScope.launch {
            val startOfMonth = now.withDayOfMonth(1)
                .toLocalDate()
                .atStartOfDay()

            // Final do mês
            val endOfMonth = now.withDayOfMonth(now.toLocalDate().lengthOfMonth())

            val filteredList = serviceT.transactionsFilter(startOfMonth, endOfMonth)
            val expensesFiltered = serviceT.getExpenses(filteredList)
            _transactions.clear()
            _transactions.addAll(expensesFiltered)
        }
    }

}