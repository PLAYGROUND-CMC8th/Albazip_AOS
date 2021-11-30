package com.playground.albazip.src.login.data

import com.google.gson.annotations.SerializedName

data class PostNewPWRequest(
    @SerializedName("phone") val phone: String,
    @SerializedName("pwd") val pwd: String
)