package com.example.albazip.src.login.data

import com.google.gson.annotations.SerializedName

data class PostSignInRequest(
    @SerializedName("phone") val phone: String,
    @SerializedName("pwd") val pwd: String
)