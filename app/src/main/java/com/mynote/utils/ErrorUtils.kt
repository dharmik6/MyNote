package com.project.app.utils

import android.content.Context
import com.google.gson.Gson
import com.google.gson.JsonParseException
import com.google.gson.reflect.TypeToken
import com.mynote.R
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException


class ErrorUtils(val context: Context) {

    fun handleError(e: Exception?, key: String?): ErrorBean {

        return if (e != null) {

            when (e) {

                is IllegalStateException -> {
                    ErrorBean(AppConstant.Error.SERVER_ERROR, e.localizedMessage, key)
                }

                is SocketTimeoutException -> {
                    ErrorBean(
                        AppConstant.Error.SERVER_ERROR,
                        context.getString(R.string.connection_timeout),
                        key
                    )
                }

                is ConnectException, is UnknownHostException -> {
                    ErrorBean(
                        AppConstant.Error.INTERNET_ERROR,
                        context.getString(R.string.no_internet),
                        key
                    )
                }

                is JsonParseException -> {
                    ErrorBean(
                        AppConstant.Error.SERVER_ERROR,
                        context.getString(R.string.something_went_wrong),
                        key
                    )
                }

                else -> {
                    handleMapError(e.message ?: "")
                    //                Error(AppConstant.Error.UNKNOWN_ERROR, e.message, key)
                }
            }

        } else ErrorBean(
            AppConstant.Error.UNKNOWN_ERROR,
            context.getString(R.string.unknown_error),
            key
        )

    }

    fun handleMapError(ErrorString: String): ErrorBean {
        try {
            val yourHashMap =
                Gson().fromJson(ErrorString, HashMap::class.java)

            try {
                if (yourHashMap != null) {
                    val keys: MutableSet<out Any> = yourHashMap.keys
                    for (key in keys) {
                        if (yourHashMap[key] != null && yourHashMap[key].toString().isNotEmpty()) {
                            try {
                                val messageList: ArrayList<String> = Gson().fromJson(
                                    Gson().toJson(yourHashMap[key]),
                                    object : TypeToken<ArrayList<String>>() {}.type
                                )

                                return ErrorBean(
                                    AppConstant.Error.UNKNOWN_ERROR,
                                    messageList[0],
                                    key.toString()
                                )

                            } catch (exception: Exception) {
                                val messageList: HashMap<*, *> = Gson().fromJson(
                                    Gson().toJson(yourHashMap["error"]),
                                    object : TypeToken<HashMap<*, *>>() {}.type
                                )

                                return ErrorBean(
                                    AppConstant.Error.UNKNOWN_ERROR,
                                    messageList[0].toString(),
                                    key.toString()
                                )
                            }
                        }
                    }
                }
            } catch (exception: Exception) {
                return ErrorBean(
                    AppConstant.Error.UNKNOWN_ERROR,
                    context.getString(R.string.something_went_wrong),
                    ""
                )
            }
        } catch (exception: Exception) {
            return ErrorBean(AppConstant.Error.UNKNOWN_ERROR, ErrorString, "")
        }
        return ErrorBean(
            AppConstant.Error.UNKNOWN_ERROR,
            context.getString(R.string.something_went_wrong),
            ""
        )
    }

}


//class ErrorUtils(private val context: Context) {
//    fun handleErrorImage(e: Throwable): ErrorBean {
//        when (e) {
//            is IllegalStateException -> {
//                return ErrorBean(
//                    e,
//                    ErrorType.ERROR_INVALID_RESPONSE.errorMessage,
//                    R.drawable.ic_unexpected_response
//                )
//            }
//            is SocketTimeoutException -> {
//                return ErrorBean(
//                    e,
//                    ErrorType.ERROR_REQUEST_TIME_OUT.errorMessage,
//                    R.drawable.ic_request_time_out
//                )
//            }
//            is UnknownHostException -> {
//                return ErrorBean(
//                    e,
//                    ErrorType.ERROR_CONNECTIVITY.errorMessage,
//                    R.drawable.ic_connectivity
//                )
//            }
//            is ConnectException -> {
//                return ErrorBean(
//                    e,
//                    ErrorType.ERROR_CONNECTIVITY.errorMessage,
//                    R.drawable.ic_connectivity
//                )
//            }
//            is HttpException -> {
//                return ErrorBean(
//                    e,
//                    ErrorType.ERROR_INVALID_RESPONSE.errorMessage,
//                    R.drawable.ic_unexpected_response
//                )
//            }
//            is JsonSyntaxException -> {
//                return ErrorBean(
//                    e,
//                    ErrorType.ERROR_INVALID_RESPONSE.errorMessage,
//                    R.drawable.ic_unexpected_response
//                )
//            }
//            else -> {
//                return ErrorBean(
//                    e,
//                    ErrorType.PARSING_ERROR.errorMessage,
//                    R.drawable.ic_parsing_error
//                )
//            }
//        }
//
//    }
//
//
//    fun handleMapError(ErrorString: String) {
//
//        val yourHashMap = Gson().fromJson(ErrorString, HashMap::class.java) as HashMap<*, *>
//
//        try {
//            if (yourHashMap != null) {
//                val keys: MutableSet<out Any> = yourHashMap.keys
//                for (key in keys) {
//                    if (yourHashMap[key] != null && !ValidationUtils.isEmptyFiled(yourHashMap[key].toString())) {
//                        try {
//                            LogX.E("----------->" + yourHashMap[key].toString())
//                            val messageList: ArrayList<String> = Gson().fromJson(
//                                Gson().toJson(yourHashMap[key]),
//                                object : TypeToken<ArrayList<String>>() {}.type
//                            )
//                            if (messageList != null) {
//                                MyApp.getAppInstance().showToast(messageList[0])
//                            }
//                        } catch (exception: Exception) {
//                            MyApp.getAppInstance().showToast(yourHashMap[key].toString())
//                        }
//                        return
//                    }
//                }
//            }
//        } catch (exception: Exception) {
//
//        }
//
//    }
//
//    fun getMapErrorString(ErrorString: String): String {
//        val value: String = ""
//
//        val yourHashMap = Gson().fromJson(ErrorString, HashMap::class.java) as HashMap<*, *>
//
//        try {
//            if (yourHashMap != null) {
//                val keys: MutableSet<out Any> = yourHashMap.keys
//                for (key in keys) {
//                    if (yourHashMap[key] != null && !ValidationUtils.isEmptyFiled(yourHashMap[key].toString())) {
//                        try {
//                            LogX.E("----------->" + yourHashMap[key].toString())
//                            val messageList: ArrayList<String> = Gson().fromJson(
//                                Gson().toJson(yourHashMap[key]),
//                                object : TypeToken<ArrayList<String>>() {}.type
//                            )
//                            if (messageList != null) {
//                                return messageList[0]
//
//                            }
//                        } catch (exception: Exception) {
//                            return yourHashMap[key].toString()
//
//                        }
//                    }
//                }
//            }
//        } catch (exception: Exception) {
//
//        }
//        return value
//    }
//
//}