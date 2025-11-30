package com.app.rastreadorfinanceiro.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.app.rastreadorfinanceiro.repository.CategoryRepository
import com.app.rastreadorfinanceiro.repository.GoalsRepository
import com.app.rastreadorfinanceiro.repository.TransactionRepository

class RastreadorViewModelFactory(
    private val transactionRepository: TransactionRepository,
    private val categoryRepository: CategoryRepository,
    private val goalsRepository: GoalsRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(TransactionViewModel::class.java)) {
            return TransactionViewModel(transactionRepository) as T
        }

        if (modelClass.isAssignableFrom(CategoryViewModel::class.java)) {
            return CategoryViewModel(categoryRepository) as T
        }

        if (modelClass.isAssignableFrom(GoalsViewModel::class.java)) {
            return GoalsViewModel(goalsRepository) as T
        }

        if (modelClass.isAssignableFrom(DashboardViewModel::class.java)) {
            return DashboardViewModel(transactionRepository, categoryRepository) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}