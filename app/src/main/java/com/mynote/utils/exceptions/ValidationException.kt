package com.dottore.app.network.exceptions

class ValidationException : Exception {
    constructor(message: String?) : super(message)
    constructor(message: String?, throwable: Throwable?) : super(message, throwable)
    constructor() : super(null, null)
}