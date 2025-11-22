package com.app.rastreadorfinanceiro.repository

import com.app.rastreadorfinanceiro.model.GoalModel

class GoalsRepository : BaseRepository<GoalModel>() {
    private val storage: MutableList<GoalModel> = mutableListOf()

    fun fetchGoals(): List<GoalModel> = storage.toList()

    fun addGoal(goal: GoalModel) {
        storage.add(goal)
    }

    fun removeGoal(goal: GoalModel) {
        storage.removeIf { it.id == goal.id }
    }

    fun updateGoal(goal: GoalModel) {
        val idx = storage.indexOfFirst { it.id == goal.id }
        if (idx >= 0) storage[idx] = goal
    }

    override fun save(data: List<GoalModel>) {
        storage.clear()
        storage.addAll(data)
    }

    override fun load(): List<GoalModel> = storage.toList()
}
