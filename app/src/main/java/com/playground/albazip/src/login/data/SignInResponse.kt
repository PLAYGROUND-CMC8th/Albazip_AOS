package com.playground.albazip.src.login.data


import com.playground.albazip.config.BaseResponse
import com.google.gson.annotations.SerializedName

data class SignInResponse(
    @SerializedName("data") val data: Token,

):BaseResponse()

data class Token(
    @SerializedName("job") val job: Int,
    @SerializedName("userFirstName") val userFirstName: String,
    @SerializedName("token") val token: ResultSignIn,
)

data class ResultSignIn(
    @SerializedName("token") val token: String,

)