package com.playground.albazip.src.register.common.data.remote

import com.playground.albazip.config.BaseResponse
import com.google.gson.annotations.SerializedName

data class PositionRegisterResponse(
    @SerializedName("data") val tokenData: Token,
): BaseResponse()
