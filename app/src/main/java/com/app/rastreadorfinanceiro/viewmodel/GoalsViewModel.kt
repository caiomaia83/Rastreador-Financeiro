package com.app.rastreadorfinanceiro.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.rastreadorfinanceiro.model.GoalModel
import com.app.rastreadorfinanceiro.repository.GoalsRepository
import com.app.rastreadorfinanceiro.service.GoalsService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID

class GoalsViewModel(
    private val repo: GoalsRepository
) : ViewModel() {

    private val service = GoalsService(repo)

    private val _goals = mutableStateListOf<GoalModel>()
    val goals: List<GoalModel> get() = _goals


    private val _balanceByCategory = MutableStateFlow<Map<String, Double>>(emptyMap())
    val balanceByCategory: StateFlow<Map<String, Double>> = _balanceByCategory.asStateFlow()

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {

            val fetchedGoals = repo.fetchGoals()
            _goals.clear()
            _goals.addAll(fetchedGoals)


            _balanceByCategory.value = service.balanceByCategory()
        }
    }

    fun addGoal(categoryId: String, amount: Double) {
        val goal = GoalModel(UUID.randomUUID().toString(), categoryId, amount)
        viewModelScope.launch {
            repo.addGoal(goal)
            refresh()
        }
    }

    fun removeGoal(goal: GoalModel) {
        viewModelScope.launch {
            repo.removeGoal(goal)
            refresh()
        }
    }

    fun updateGoal(goal: GoalModel) {
        viewModelScope.launch {
            repo.updateGoal(goal)
            refresh()
        }
    }

    
    fun sumTotalGoals(): Double = service.sumTotalGoals(_goals)
}