package com.playground.albazip.src.mypage.common.setting.editinfo.data

import com.playground.albazip.config.BaseResponse
import com.google.gson.annotations.SerializedName

data class LoadInfoResponse (
    @SerializedName("data") val data: LoadInfoData,
):BaseResponse()

data class LoadInfoData(
    @SerializedName("firstName") val firstName: String,
    @SerializedName("lastName") val lastName: String,
    @SerializedName("birthyear") val birthyear: String,
    @SerializedName("gender") val gender: Int,
    @SerializedName("phone") val phone: String,
)