package com.app.rastreadorfinanceiro.repository

import android.graphics.Color as AndroidColor // Alias para não confundir com Compose Color se necessário
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.app.rastreadorfinanceiro.data.CategoryDao
import com.app.rastreadorfinanceiro.data.TransactionDao
import com.app.rastreadorfinanceiro.data.TransactionEntity
import com.app.rastreadorfinanceiro.model.CategoryModel
import com.app.rastreadorfinanceiro.model.ExpenseModel
import com.app.rastreadorfinanceiro.model.IncomeModel
import com.app.rastreadorfinanceiro.model.TransactionModel

// Repositório agora recebe OS DOIS DAOs para poder cruzar as informações
class TransactionRepository(
    private val transactionDao: TransactionDao,
    private val categoryDao: CategoryDao
) {

    // Função para buscar tudo e converter para o formato que a tela entende
    
    suspend fun fetchTransactions(): List<TransactionModel> {
        // 1. Busca todas as transações do banco
        val entities = transactionDao.getAll()
        
        // 2. Busca todas as categorias para podermos consultar (cache simples)
        val categoryEntities = categoryDao.getAll()
        
        // 3. Converte cada Entidade do banco para um Modelo de Tela
        return entities.map { entity ->
            if (entity.type == "INCOME") {
                // Se for Receita, é simples, não tem categoria
                IncomeModel(
                    id = entity.id,
                    amount = entity.amount,
                    date = entity.date,
                    description = entity.description
                )
            } else {
                // Se for Despesa, precisamos encontrar a categoria certa pelo ID
                val categoryEntity = categoryEntities.find { it.id == entity.categoryId }
                
                // Se achou no banco, cria o modelo. Se não (deu erro ou foi deletada), cria uma padrão.
                val categoryModel = if (categoryEntity != null) {
                    CategoryModel(
                        id = categoryEntity.id,
                        name = categoryEntity.name,
                        color = Color(categoryEntity.colorArgb) // Converte Int do banco para Color do Compose
                    )
                } else {
                    CategoryModel(id = "unknown", name = "Desconhecido", color = Color.Gray)
                }

                ExpenseModel(
                    id = entity.id,
                    amount = entity.amount,
                    date = entity.date,
                    description = entity.description,
                    category = categoryModel // Aqui entregamos a categoria completa!
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
            categoryId = null // Receita não tem categoria vinculada
        )
        transactionDao.insert(entity)
    }

    // Adicionar Despesa (Aqui estava faltando!)
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
        // Precisamos criar uma entidade temporária só com o ID para o Room deletar
        // Ou podemos buscar antes. Mas o jeito mais simples se o DAO aceita objeto é:
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
    
    // Atualizar segue a mesma lógica do Adicionar, pois usamos REPLACE no Dao
    suspend fun updateTransaction(transaction: TransactionModel) {
        if (transaction is IncomeModel) {
            addIncome(transaction) 
        } else if (transaction is ExpenseModel) {
            addExpense(transaction)
        }
    }
}