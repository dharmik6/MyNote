package com.mynote.data.response

data class AuthUserResponse(
    var providerId : Int? = null,
    var uid : Int? = null ,
    var name :String? = null ,
    var email :String? = null ,
    var photoUrl : String? = null
)
