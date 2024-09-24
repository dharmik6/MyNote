package com.mynote.data.param

import com.google.gson.annotations.SerializedName

data class UserDataParam(
    @SerializedName("name")
    val name : String? = null,
    @SerializedName("email")
    val email : String? = null,
    @SerializedName("password")
    val password : String? = null,
    @SerializedName("profile_image")
    val profileImage : String? = null,
)
