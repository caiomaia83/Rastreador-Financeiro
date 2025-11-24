package com.app.rastreadorfinanceiro.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.app.rastreadorfinanceiro.model.CategoryModel
import com.app.rastreadorfinanceiro.repository.CategoryRepository
import java.util.UUID

class CategoryViewModel(
    private val repo: CategoryRepository = CategoryRepository()
) : ViewModel() {

    private val _categories = mutableStateListOf<CategoryModel>()
    val categories: List<CategoryModel> get() = _categories

    init {
        _categories.clear()
        _categories.addAll(repo.fetchCategories())
    }

    fun refresh() {
        _categories.clear()
        _categories.addAll(repo.fetchCategories())
    }

    fun addCategory(name: String, color: androidx.compose.ui.graphics.Color) {
        val category = CategoryModel(UUID.randomUUID().toString(), name, color)
        repo.addCategory(category)
        _categories.add(category)
    }

    fun removeCategory(category: CategoryModel) {
        repo.removeCategory(category)
        _categories.removeIf { it.id == category.id }
    }

    fun updateCategory(category: CategoryModel) {
        repo.updateCategory(category)
        val idx = _categories.indexOfFirst { it.id == category.id }
        if (idx >= 0) _categories[idx] = category
    }
}
