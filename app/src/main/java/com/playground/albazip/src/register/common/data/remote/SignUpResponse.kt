package com.playground.albazip.src.register.common.data.remote

import com.playground.albazip.config.BaseResponse
import com.google.gson.annotations.SerializedName

data class SignUpResponse(
    @SerializedName("data") val data : Data,
):BaseResponse()

data class Data(
    @SerializedName("token") val token: Token,
)

data class Token(
    @SerializedName("token") val token: String,
)