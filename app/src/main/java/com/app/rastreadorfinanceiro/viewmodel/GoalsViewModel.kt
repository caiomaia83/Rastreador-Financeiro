package com.app.rastreadorfinanceiro.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.app.rastreadorfinanceiro.model.GoalModel
import com.app.rastreadorfinanceiro.model.CategoryModel
import com.app.rastreadorfinanceiro.repository.GoalsRepository
import com.app.rastreadorfinanceiro.service.GoalsService
import java.util.UUID

class GoalsViewModel(
    private val repo: GoalsRepository = GoalsRepository(),
    private val service: GoalsService = GoalsService(repo)
) : ViewModel() {

    private val _goals = mutableStateListOf<GoalModel>()
    val goals: List<GoalModel> get() = _goals

    init {
        _goals.clear()
        _goals.addAll(repo.fetchGoals())
    }

    fun refresh() {
        _goals.clear()
        _goals.addAll(repo.fetchGoals())
    }

    fun addGoal(categoryId: String, amount: Double) {
        val goal = GoalModel(UUID.randomUUID().toString(), categoryId, amount)
        repo.addGoal(goal)
        _goals.add(goal)
    }

    fun removeGoal(goal: GoalModel) {
        repo.removeGoal(goal)
        _goals.removeIf { it.id == goal.id }
    }

    fun updateGoal(goal: GoalModel) {
        repo.updateGoal(goal)
        val idx = _goals.indexOfFirst { it.id == goal.id }
        if (idx >= 0) _goals[idx] = goal
    }

    fun sumTotalGoals(): Double = service.sumTotalGoals(_goals)

    fun balanceByCategory(): Map<String, Double> = service.balanceByCategory()
}
