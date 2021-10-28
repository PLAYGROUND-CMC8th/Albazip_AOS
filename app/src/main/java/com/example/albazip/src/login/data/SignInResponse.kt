package com.example.albazip.src.login.data

import com.google.gson.annotations.SerializedName

data class SignInResponse(
    @SerializedName("data") val resultSignIn: ResultSignIn,
    @SerializedName("message") val message: String,
)

data class ResultSignIn(
    @SerializedName("positionInfo") val positionInfo: String,
    @SerializedName("token") val token: Int,
)