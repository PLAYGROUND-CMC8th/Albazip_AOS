package com.playground.albazip.src.home.manager.data

import com.playground.albazip.config.BaseResponse
import com.google.gson.annotations.SerializedName

data class HomeMPerWorkResponse (
    @SerializedName("data")val data:ArrayList<HomeMPerResult>
        ):BaseResponse()

data class HomeMPerResult(
    @SerializedName("workerId")val workerId:Int,
    @SerializedName("workerTitle")val workerTitle:String,
    @SerializedName("totalCount")val totalCount:Int,
    @SerializedName("completeCount")val completeCount:Int,
)