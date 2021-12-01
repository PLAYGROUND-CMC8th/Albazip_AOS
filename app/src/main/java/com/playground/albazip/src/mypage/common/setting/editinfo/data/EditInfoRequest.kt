package com.playground.albazip.src.mypage.common.setting.editinfo.data

import com.google.gson.annotations.SerializedName

data class EditInfoRequest (
    @SerializedName("firstName") val firstName: String,
    @SerializedName("lastName") val lastName: String,
    @SerializedName("birthyear") val birthyear: String,
    @SerializedName("gender") val gender: String,
)