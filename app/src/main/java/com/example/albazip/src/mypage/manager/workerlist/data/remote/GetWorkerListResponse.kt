package com.example.albazip.src.mypage.manager.workerlist.data.remote

import com.example.albazip.config.BaseResponse
import com.google.gson.annotations.SerializedName

data class GetWorkerListResponse (
    @SerializedName("rank") val data : ArrayList<WorkerListData>?,
):BaseResponse()

data class WorkerListData(
    @SerializedName("positionId") val positionId : String,
    @SerializedName("workerId") val workerId : String,
    @SerializedName("status") val status : String,
    @SerializedName("imagePath") val imagePath : String,
    @SerializedName("title") val title : String,
    @SerializedName("firstName") val firstName : String,
)


