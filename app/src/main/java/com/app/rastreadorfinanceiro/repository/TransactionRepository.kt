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

        val entities = transactionDao.getAll()


        val categoryEntities = categoryDao.getAll()


        return entities.map { entity ->
            if (entity.type == "INCOME") {

                IncomeModel(
                    id = entity.id,
                    amount = entity.amount,
                    date = entity.date,
                    description = entity.description
                )
            } else {

                val categoryEntity = categoryEntities.find { it.id == entity.categoryId }


                val categoryModel = if (categoryEntity != null) {
                    CategoryModel(
                        id = categoryEntity.id,
                        name = categoryEntity.name,
                        color = Color(categoryEntity.colorArgb),
                        budgetLimit = categoryEntity.budgetLimit
                    )
                } else {

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


    suspend fun addExpense(expense: ExpenseModel) {
        val entity = TransactionEntity(
            id = expense.id,
            amount = expense.amount,
            description = expense.description,
            date = expense.date,
            type = "EXPENSE",
            categoryId = expense.category.id
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