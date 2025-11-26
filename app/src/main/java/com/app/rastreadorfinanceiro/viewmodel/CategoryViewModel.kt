package com.app.rastreadorfinanceiro.viewmodel

import androidx.compose.runtime.mutableStateListOf
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
            _categories.clear()
            _categories.addAll(repo.fetchCategories())
        }
    }

    fun addCategory(name: String, color: androidx.compose.ui.graphics.Color) {
        val category = CategoryModel(UUID.randomUUID().toString(), name, color)
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