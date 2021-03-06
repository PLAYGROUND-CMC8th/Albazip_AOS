package com.playground.albazip.src.mypage.common.workerdata.taskinfo.data

import com.playground.albazip.config.BaseResponse
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
    @SerializedName("writer_name")val writer_name:String,
    @SerializedName("writer_position")val writer_position:String,
    @SerializedName("register_date")val register_date:String,
)

data class CompleteTaskData(
    @SerializedName("title")val title:String,
    @SerializedName("content")val content:String,
    @SerializedName("complete_date")val complete_date:String
)