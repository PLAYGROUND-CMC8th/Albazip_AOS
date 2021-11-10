package com.example.albazip.src.register.common.data.remote

import com.google.gson.annotations.SerializedName

data class PhoneCheckResponse(
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,
)