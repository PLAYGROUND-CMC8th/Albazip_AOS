package com.example.albazip.src.mypage.common.workerdata.taskinfo.data

import com.example.albazip.config.BaseResponse
import com.google.gson.annotations.SerializedName

data class GetDayTaskResponse(
    @SerializedName("data")val data:DayTaskResult
):BaseResponse()


data class DayTaskResult(
    @SerializedName("nonCompleteTaskData")val nonCompleteTaskData:ArrayList<NonCompleteTaskData>,
    @SerializedName("completeTaskData") val completeTaskData:ArrayList<CompleteTaskData>
)

data class NonCompleteTaskData(
    @SerializedName("title")val title:String,
    @SerializedName("content")val content:String,
)

data class CompleteTaskData(
    @SerializedName("title")val title:String,
    @SerializedName("content")val content:String,
    @SerializedName("complete_date")val complete_date:String
)