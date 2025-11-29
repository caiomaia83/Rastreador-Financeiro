package com.app.rastreadorfinanceiro.repository

import android.graphics.Color as AndroidColor
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.app.rastreadorfinanceiro.data.CategoryDao
import com.app.rastreadorfinanceiro.data.TransactionDao
import com.app.rastreadorfinanceiro.data.TransactionEntity
import com.app.rastreadorfinanceiro.model.CategoryModel
import com.app.rastreadorfinanceiro.model.ExpenseModel
import com.app.rastreadorfinanceiro.model.IncomeModel
import com.app.rastreadorfinanceiro.model.TransactionModel

class TransactionRepository(
    private val transactionDao: TransactionDao,
    private val categoryDao: CategoryDao
) {

    suspend fun fetchTransactions(): List<TransactionModel> {
        // 1. Busca todas as transações do banco
        val entities = transactionDao.getAll()

        // 2. Busca todas as categorias para podermos consultar
        val categoryEntities = categoryDao.getAll()

        // 3. Converte cada Entidade do banco para um Modelo de Tela
        return entities.map { entity ->
            if (entity.type == "INCOME") {
                // Se for Receita, não tem categoria vinculada
                IncomeModel(
                    id = entity.id,
                    amount = entity.amount,
                    date = entity.date,
                    description = entity.description
                )
            } else {
                // Se for Despesa, precisamos encontrar a categoria certa pelo ID
                val categoryEntity = categoryEntities.find { it.id == entity.categoryId }

                // Se achou no banco, cria o modelo preenchendo o novo campo budgetLimit
                val categoryModel = if (categoryEntity != null) {
                    CategoryModel(
                        id = categoryEntity.id,
                        name = categoryEntity.name,
                        color = Color(categoryEntity.colorArgb),
                        budgetLimit = categoryEntity.budgetLimit // <--- ATUALIZADO AQUI
                    )
                } else {
                    // Categoria padrão caso não encontre (ex: foi deletada)
                    CategoryModel(
                        id = "unknown",
                        name = "Desconhecido",
                        color = Color.Gray,
                        budgetLimit = null
                    )
                }

                ExpenseModel(
                    id = entity.id,
                    amount = entity.amount,
                    date = entity.date,
                    description = entity.description,
                    category = categoryModel
                )
            }
        }
    }

    // Adicionar Receita
    suspend fun addIncome(income: IncomeModel) {
        val entity = TransactionEntity(
            id = income.id,
            amount = income.amount,
            description = income.description,
            date = income.date,
            type = "INCOME",
            categoryId = null
        )
        transactionDao.insert(entity)
    }

    // Adicionar Despesa
    suspend fun addExpense(expense: ExpenseModel) {
        val entity = TransactionEntity(
            id = expense.id,
            amount = expense.amount,
            description = expense.description,
            date = expense.date,
            type = "EXPENSE",
            categoryId = expense.category.id // Salvamos apenas o ID da categoria
        )
        transactionDao.insert(entity)
    }

    suspend fun removeTransaction(transaction: TransactionModel) {
        val type = if (transaction is IncomeModel) "INCOME" else "EXPENSE"
        val categoryId = if (transaction is ExpenseModel) transaction.category.id else null

        val entity = TransactionEntity(
            id = transaction.id,
            amount = transaction.amount,
            description = transaction.description,
            date = transaction.date,
            type = type,
            categoryId = categoryId
        )
        transactionDao.delete(entity)
    }

    suspend fun updateTransaction(transaction: TransactionModel) {
        if (transaction is IncomeModel) {
            addIncome(transaction)
        } else if (transaction is ExpenseModel) {
            addExpense(transaction)
        }
    }
}