package com.app.rastreadorfinanceiro.service

import com.app.rastreadorfinanceiro.model.GoalModel
import com.app.rastreadorfinanceiro.model.CategoryModel
import com.app.rastreadorfinanceiro.repository.GoalsRepository

class GoalsService(
    private val repo: GoalsRepository
) : IGoalsService {

    override fun loadGoals(goals: List<GoalModel>, categories: List<CategoryModel>): Map<String, Double> {
        return goals.associate { it.categoryId to it.amount }
    }


    override fun loadPercentExpenseGoals(goals: Map<String, Double>, goalsList: List<GoalModel>, maps: Map<String, Double>): Map<String, Double> {
        // placeholder impl
        return goals
    }

    override fun balanceByCategory(): Map<String, Double> {
        val goals = repo.fetchGoals()
        return goals.groupBy { it.categoryId }.mapValues { entry -> entry.value.sumOf { it.amount } }
    }

    override fun sumTotalGoals(goals: List<GoalModel>): Double = goals.sumOf { it.amount }
}
