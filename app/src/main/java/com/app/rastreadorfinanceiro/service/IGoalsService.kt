package com.app.rastreadorfinanceiro.service

import com.app.rastreadorfinanceiro.model.GoalModel
import com.app.rastreadorfinanceiro.model.CategoryModel

interface IGoalsService {
    fun loadGoals(goals: List<GoalModel>, categories: List<CategoryModel>): Map<String, Double>
    fun loadPercentExpenseGoals(goals: Map<String, Double>, goalsList: List<GoalModel>, maps: Map<String, Double>): Map<String, Double>
    fun balanceByCategory(): Map<String, Double>
    fun sumTotalGoals(goals: List<GoalModel>): Double
}
