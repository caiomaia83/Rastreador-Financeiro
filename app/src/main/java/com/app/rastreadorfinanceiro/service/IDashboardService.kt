package com.app.rastreadorfinanceiro.service

import androidx.compose.ui.graphics.Color


data class CategoryExpense(
    val name: String,
    val value: Double,
    val color: Color
)

interface IDashboardService {
    suspend fun loadExpensesByCategory(): List<CategoryExpense>
}