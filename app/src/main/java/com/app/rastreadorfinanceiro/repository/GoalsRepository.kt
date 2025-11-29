package com.app.rastreadorfinanceiro.repository


import com.app.rastreadorfinanceiro.data.GoalDao
import com.app.rastreadorfinanceiro.data.GoalEntity
import com.app.rastreadorfinanceiro.model.GoalModel

class GoalsRepository(private val dao: GoalDao) {

    suspend fun fetchGoals(): List<GoalModel> {
        return dao.getAll().map { entity ->
            GoalModel(
                id = entity.id,
                categoryId = entity.categoryId,
                amount = entity.amount
            )
        }
    }

    suspend fun addGoal(goal: GoalModel) {
        val entity = GoalEntity(
            id = goal.id,
            categoryId = goal.categoryId,
            amount = goal.amount
        )
        dao.insert(entity)
    }

    suspend fun removeGoal(goal: GoalModel) {
        val entity = GoalEntity(
            id = goal.id,
            categoryId = goal.categoryId,
            amount = goal.amount
        )
        dao.delete(entity)
    }

    suspend fun updateGoal(goal: GoalModel) {
        // Como o DAO usa REPLACE, inserir de novo atualiza o registro
        addGoal(goal)
    }
}