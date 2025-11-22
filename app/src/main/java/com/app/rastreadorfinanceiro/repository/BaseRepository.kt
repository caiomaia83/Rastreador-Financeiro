package com.app.rastreadorfinanceiro.repository

abstract class BaseRepository<T> {
    open fun save(data: List<T>) {}

    open fun load(): List<T> = emptyList()
}
