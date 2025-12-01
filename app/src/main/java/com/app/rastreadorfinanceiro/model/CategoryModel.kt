package com.app.rastreadorfinanceiro.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

data class CategoryModel(
    val id: String,
    val name: String,
    val color: Color,
    val budgetLimit: Double? = null,
    val icon: ImageVector = Icons.Default.Add
)