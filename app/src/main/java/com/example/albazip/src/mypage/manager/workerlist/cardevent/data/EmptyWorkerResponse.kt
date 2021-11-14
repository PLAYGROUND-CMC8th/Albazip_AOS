package com.example.albazip.src.mypage.manager.workerlist.cardevent.data

import com.example.albazip.config.BaseResponse
import com.example.albazip.src.mypage.worker.init.data.PositionInfo
import com.google.gson.annotations.SerializedName

data class EmptyWorkerResponse(
    @SerializedName("data") val data: EmptyWData,
) : BaseResponse()

data class EmptyWData(
    @SerializedName("positionProfile") val positionProfile: PositionProfileInfo,
    @SerializedName("workerInfo") val workerInfo: WorkerInfo,
    @SerializedName("positionInfo") val positionInfo: PositionInfo,
    @SerializedName("positionTaskList") val positionTaskList: ArrayList<PositionTaskList>,
)

data class PositionProfileInfo(
    @SerializedName("rank") val rank: String,
    @SerializedName("title") val title: String,
    @SerializedName("imagePath") val imagePath: String,
    @SerializedName("firstName") val firstName: String,
)

data class WorkerInfo(
    @SerializedName("positionInfo") val positionInfo: PositionInfo
) {
    data class PositionInfo(
        @SerializedName("code") val code: String
    )
}

//data class PositionTaskList(
//        @SerializedName("firstName") val firstName: ArrayList<String>,
//)