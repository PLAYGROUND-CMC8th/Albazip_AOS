package com.example.albazip.src.mypage.common.workerdata.cotask.data

import com.example.albazip.config.BaseResponse
import com.google.gson.annotations.SerializedName

data class GetCoTaskInfoResponse(
    @SerializedName("data") val data: ArrayList<CoTaskResult>,
) : BaseResponse()

data class CoTaskResult(
    @SerializedName("title") val title: String,
    @SerializedName("content") val content: String,
    @SerializedName("year") val year: String,
    @SerializedName("month") val month: String,
    @SerializedName("date") val date: String,
    @SerializedName("complete_date") val complete_date: String
)