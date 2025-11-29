package com.app.rastreadorfinanceiro.viewmodel

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

            // LÓGICA DE PRÉ-POPULAÇÃO (SEED)
            // Se o banco estiver vazio, cria as categorias padrão
            if (currentList.isEmpty()) {
                val defaultCategories = listOf(
                    CategoryModel(UUID.randomUUID().toString(), "Alimentação", Color(0xFFEF5350)), // Vermelho Claro
                    CategoryModel(UUID.randomUUID().toString(), "Lazer", Color(0xFF42A5F5)),       // Azul
                    CategoryModel(UUID.randomUUID().toString(), "Transporte", Color(0xFFFFCA28)),  // Amarelo
                    CategoryModel(UUID.randomUUID().toString(), "Moradia", Color(0xFF66BB6A)),     // Verde
                    CategoryModel(UUID.randomUUID().toString(), "Saúde", Color(0xFFAB47BC)),       // Roxo
                    CategoryModel(UUID.randomUUID().toString(), "Outros", Color(0xFF8D6E63))       // Marrom
                )

                // Salva cada uma no banco (o limite será null automaticamente)
                defaultCategories.forEach { repo.addCategory(it) }

                // Atualiza a lista da tela com as novas categorias
                _categories.clear()
                _categories.addAll(repo.fetchCategories())
            } else {
                // Se já existirem categorias, apenas carrega
                _categories.clear()
                _categories.addAll(currentList)
            }
        }
    }

    // Função atualizada para aceitar o limite (budgetLimit)
    fun addCategory(name: String, color: Color, limit: Double? = null) {
        // Cria a categoria passando o limite (pode ser null ou um valor)
        val category = CategoryModel(UUID.randomUUID().toString(), name, color, limit)
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