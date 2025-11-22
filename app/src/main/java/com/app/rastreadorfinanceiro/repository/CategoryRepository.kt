package com.app.rastreadorfinanceiro.repository

import com.app.rastreadorfinanceiro.model.CategoryModel

class CategoryRepository : BaseRepository<CategoryModel>() {
    private val storage: MutableList<CategoryModel> = mutableListOf()

    fun fetchCategories(): List<CategoryModel> = storage.toList()

    fun addCategory(category: CategoryModel) {
        storage.add(category)
    }

    fun removeCategory(category: CategoryModel) {
        storage.removeIf { it.id == category.id }
    }

    fun updateCategory(category: CategoryModel) {
        val idx = storage.indexOfFirst { it.id == category.id }
        if (idx >= 0) storage[idx] = category
    }

    override fun save(data: List<CategoryModel>) {
        storage.clear()
        storage.addAll(data)
    }

    override fun load(): List<CategoryModel> = storage.toList()
}
