package com.project.app.utils

import com.mynote.R


enum class ErrorType(var error: Int) {

    ERROR_INVALID_RESPONSE(R.string.unexpected_response),
    ERROR_REQUEST_TIME_OUT(R.string.connection_timeout),
    ERROR_CONNECTIVITY(R.string.no_internet),
    PARSING_ERROR(R.string.parsing_error);

    var errorMessage: Int = error

}
