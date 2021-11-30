package com.playground.albazip.src.register.common.data.remote

import com.google.gson.annotations.SerializedName

data class PostSignUpRequest(
    @SerializedName("birthyear") val birthyear : String,
    @SerializedName("firstName") val firstName : String,
    @SerializedName("gender") val gender : String,
    @SerializedName("lastName") val lastName : String,
    @SerializedName("phone") val phone : String,
    @SerializedName("pwd") val pwd : String,
)