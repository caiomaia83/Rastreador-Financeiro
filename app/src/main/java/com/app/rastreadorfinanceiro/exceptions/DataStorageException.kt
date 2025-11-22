package com.app.rastreadorfinanceiro.exceptions

open class DataStorageException(message: String? = null, cause: Throwable? = null) : Exception(message, cause)

class FullStorageException(message: String? = "Storage is full") : DataStorageException(message)
