package com.example.albazip.src.register.common.data.remote

import com.google.gson.annotations.SerializedName

data class SignUpResponse(
    @SerializedName("data") val data : Data,
    @SerializedName("message") val message: String,
)

data class Data(
    @SerializedName("token") val token: String,
)