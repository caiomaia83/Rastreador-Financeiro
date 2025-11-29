package com.app.rastreadorfinanceiro.service

interface IDashboardService {
    // Nova função completa: Busca e calcula os dados para o gráfico
    suspend fun loadExpensesByCategory(): Map<String, Double>
}