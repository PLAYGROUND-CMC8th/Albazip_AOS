package com.playground.albazip.src.mypage.common.workerdata.taskinfo.data

import com.playground.albazip.config.BaseResponse
import com.google.gson.annotations.SerializedName

data class GetTaskRateResponse (
    @SerializedName("data")val data:TaskRateResult
        ):BaseResponse()

data class TaskRateResult(
    @SerializedName("taskRate")val taskRate:TaskRate,
    @SerializedName("taskData")val taskData:ArrayList<TaskData>
)

data class TaskRate(
    @SerializedName("completeTaskCount")val completeTaskCount:Int,
    @SerializedName("totalTaskCount")val totalTaskCount:Int
)

data class TaskData(
    @SerializedName("year")val year:String,
    @SerializedName("month")val month:String,
    @SerializedName("totalCount")val totalCount:Int,
    @SerializedName("completeCount")val completeCount:Int
)