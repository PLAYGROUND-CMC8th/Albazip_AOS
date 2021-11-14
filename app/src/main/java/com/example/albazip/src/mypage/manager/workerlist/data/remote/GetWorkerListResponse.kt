package com.example.albazip.src.mypage.manager.workerlist.data.remote

import com.example.albazip.config.BaseResponse
import com.google.gson.annotations.SerializedName

data class GetWorkerListResponse (
    @SerializedName("data") val data : ArrayList<WorkerListData>?,
):BaseResponse()

data class WorkerListData(
    @SerializedName("positionId") val positionId : Int,
    @SerializedName("rank") val rank : String,
    @SerializedName("status") val status : Int,
    @SerializedName("image_path") val image_path : String?,
    @SerializedName("title") val title : String,
    @SerializedName("first_name") val first_name : String,
)


