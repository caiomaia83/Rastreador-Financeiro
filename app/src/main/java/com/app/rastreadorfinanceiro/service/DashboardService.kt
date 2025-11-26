package com.app.rastreadorfinanceiro.service

import com.app.rastreadorfinanceiro.model.ExpenseModel
import com.app.rastreadorfinanceiro.repository.CategoryRepository
import com.app.rastreadorfinanceiro.repository.TransactionRepository

class DashboardService(
    private val transactionRepo: TransactionRepository,
    private val categoryRepo: CategoryRepository
) : IDashboardService {

    override suspend fun loadExpensesByCategory(): Map<String, Double> {
        // 1. Busca dados do banco
        val transactions = transactionRepo.fetchTransactions()
        val categories = categoryRepo.fetchCategories()

        // 2. Filtra apenas despesas
        val expenses = transactions.filterIsInstance<ExpenseModel>()

        // 3. Agrupa por Categoria (ID) e soma
        val expensesById = expenses.groupBy { it.category.id }
            .mapValues { entry -> entry.value.sumOf { it.amount } }

        // 4. Traduz ID para NOME da categoria (para o gráfico)
        return expensesById.mapKeys { (id, _) ->
            categories.find { it.id == id }?.name ?: "Outros"
        }
    }
}