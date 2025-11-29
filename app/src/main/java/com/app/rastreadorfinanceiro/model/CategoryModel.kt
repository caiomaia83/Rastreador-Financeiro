package com.app.rastreadorfinanceiro.model

import androidx.compose.ui.graphics.Color

data class CategoryModel(
    val id: String,
    val name: String,
    val color: Color,
    val budgetLimit: Double? = null // Novo campo
)