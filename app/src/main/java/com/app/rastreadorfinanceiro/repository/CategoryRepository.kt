package com.app.rastreadorfinanceiro.repository

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.app.rastreadorfinanceiro.data.CategoryDao
import com.app.rastreadorfinanceiro.data.CategoryEntity
import com.app.rastreadorfinanceiro.model.CategoryModel
import com.app.rastreadorfinanceiro.utils.IconConverter


class CategoryRepository(private val dao: CategoryDao) {

    suspend fun fetchCategories(): List<CategoryModel> {
        val entities = dao.getAll()
        return entities.map { entity ->
            CategoryModel(
                id = entity.id,
                name = entity.name,
                color = Color(entity.colorArgb),
                budgetLimit = entity.budgetLimit,
                icon = IconConverter.stringToIcon(entity.iconName)
            )
        }
    }

    // Converte o modelo da tela para entidade do banco e salva
    suspend fun addCategory(category: CategoryModel) {
        val entity = CategoryEntity(
            id = category.id,
            name = category.name,
            colorArgb = category.color.toArgb(),
            budgetLimit = category.budgetLimit,
            iconName = IconConverter.iconToString(category.icon)
        )
        dao.insert(entity)
    }


    suspend fun removeCategory(category: CategoryModel) {
        val entity = CategoryEntity(
            id = category.id,
            name = category.name,
            colorArgb = category.color.toArgb(),
            budgetLimit = category.budgetLimit,
            iconName = IconConverter.iconToString(category.icon)
        )
        dao.delete(entity)
    }

    suspend fun updateCategory(category: CategoryModel) {
        addCategory(category)
    }
}