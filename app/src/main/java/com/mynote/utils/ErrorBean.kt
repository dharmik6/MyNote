package com.project.app.utils

data class ErrorBean(
    var errorCode: Int = 0,
    val errorMessage: String? = "",
    val errorKey: String? = "",
    val errorTag: String? = null,
    val error: String? = null,
    val errorImage: Int = 0,
)