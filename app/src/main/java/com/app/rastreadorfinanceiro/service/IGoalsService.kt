package com.app.rastreadorfinanceiro.service

import com.app.rastreadorfinanceiro.model.CategoryModel
import com.app.rastreadorfinanceiro.model.GoalModel

interface IGoalsService {
    fun loadGoals(goals: List<GoalModel>, categories: List<CategoryModel>): Map<String, Double>

    fun loadPercentExpenseGoals(
        goals: Map<String, Double>,
        goalsList: List<GoalModel>,
        maps: Map<String, Double>
    ): Map<String, Double>

    // ADICIONE 'suspend' AQUI:
    suspend fun balanceByCategory(): Map<String, Double>

    fun sumTotalGoals(goals: List<GoalModel>): Double
}