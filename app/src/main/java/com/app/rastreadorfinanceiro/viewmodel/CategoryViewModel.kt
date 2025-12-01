package com.app.rastreadorfinanceiro.viewmodel

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.rastreadorfinanceiro.model.CategoryModel
import com.app.rastreadorfinanceiro.repository.CategoryRepository
import kotlinx.coroutines.launch
import java.util.UUID

class CategoryViewModel(
    private val repo: CategoryRepository
) : ViewModel() {

    private val _categories = mutableStateListOf<CategoryModel>()
    val categories: List<CategoryModel> get() = _categories

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            val currentList = repo.fetchCategories()

            // Se o banco estiver vazio, cria as categorias padrão
            if (currentList.isEmpty()) {
                val defaultCategories = listOf(
                    CategoryModel(UUID.randomUUID().toString(), "Alimentação", Color(0xFFEF5350), null, Icons.Default.Place), // Vermelho Claro
                    CategoryModel(UUID.randomUUID().toString(), "Lazer", Color(0xFF42A5F5), null, Icons.Default.Star),       // Azul
                    CategoryModel(UUID.randomUUID().toString(), "Transporte", Color(0xFFFFCA28), null, Icons.Default.Phone),  // Amarelo
                    CategoryModel(UUID.randomUUID().toString(), "Moradia", Color(0xFF66BB6A), null, Icons.Default.Home),     // Verde
                    CategoryModel(UUID.randomUUID().toString(), "Saúde", Color(0xFFAB47BC), null, Icons.Default.Favorite),       // Roxo
                    CategoryModel(UUID.randomUUID().toString(), "Outros", Color(0xFF8D6E63), null, Icons.Default.Settings)       // Marrom
                )


                defaultCategories.forEach { repo.addCategory(it) }


                _categories.clear()
                _categories.addAll(repo.fetchCategories())
            } else {
                // Se já existirem categorias, apenas carrega
                _categories.clear()
                _categories.addAll(currentList)
            }
        }
    }


    fun addCategory(name: String, color: Color, limit: Double? = null, icon: androidx.compose.ui.graphics.vector.ImageVector) {

        val category = CategoryModel(UUID.randomUUID().toString(), name, color, limit, icon)
        viewModelScope.launch {
            repo.addCategory(category)
            refresh()
        }
    }

    fun removeCategory(category: CategoryModel) {
        viewModelScope.launch {
            repo.removeCategory(category)
            refresh()
        }
    }

    fun updateCategory(category: CategoryModel) {
        viewModelScope.launch {
            repo.updateCategory(category)
            refresh()
        }
    }
}