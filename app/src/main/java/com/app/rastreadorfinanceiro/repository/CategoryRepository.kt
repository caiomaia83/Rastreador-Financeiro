package com.app.rastreadorfinanceiro.repository

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.app.rastreadorfinanceiro.data.CategoryDao
import com.app.rastreadorfinanceiro.data.CategoryEntity
import com.app.rastreadorfinanceiro.model.CategoryModel

class CategoryRepository(private val dao: CategoryDao) {

    // Busca do banco e converte para o modelo da tela
    suspend fun fetchCategories(): List<CategoryModel> {
        val entities = dao.getAll()
        return entities.map { entity ->
            CategoryModel(
                id = entity.id,
                name = entity.name,
                color = Color(entity.colorArgb) // Converte Int -> Color
            )
        }
    }

    // Converte o modelo da tela para entidade do banco e salva
    suspend fun addCategory(category: CategoryModel) {
        val entity = CategoryEntity(
            id = category.id,
            name = category.name,
            colorArgb = category.color.toArgb() // Converte Color -> Int
        )
        dao.insert(entity)
    }

    suspend fun removeCategory(category: CategoryModel) {
        val entity = CategoryEntity(
            id = category.id,
            name = category.name,
            colorArgb = category.color.toArgb()
        )
        dao.delete(entity)
    }

    suspend fun updateCategory(category: CategoryModel) {
        // Como configuramos o DAO com OnConflictStrategy.REPLACE,
        // basta inserir novamente com o mesmo ID que ele atualiza.
        addCategory(category)
    }
}