package com.example.albazip.config

import com.google.gson.annotations.SerializedName

// 반복되는 리스폰스 내용 중복을 줄이기 위해 사용. 리스폰스 데이터 클래스를 만들때 상속해서 사용합니다.
open class BaseResponse(
    @SerializedName("code") val code: Int = 0,
    @SerializedName("message") val message: String? = null
)