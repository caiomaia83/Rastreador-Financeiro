package com.app.rastreadorfinanceiro.exceptions

class InsertingNegativeIncomeException(message: String? = null) : Exception(message) {
    companion object {
        fun makePositive(value: Double): Double = kotlin.math.abs(value)
    }
}
