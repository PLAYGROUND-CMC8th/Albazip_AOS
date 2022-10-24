package com.playground.albazip.src.update.runtime.data

import com.playground.albazip.config.BaseResponse

data class ResponseMSignUp(
    val data: ResultMSignUp
) : BaseResponse() {
    data class ResultMSignUp(val token: String)
}