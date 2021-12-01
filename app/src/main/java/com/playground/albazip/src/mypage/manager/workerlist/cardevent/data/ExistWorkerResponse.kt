package com.playground.albazip.src.mypage.manager.workerlist.cardevent.data

import com.playground.albazip.config.BaseResponse
import com.playground.albazip.src.mypage.worker.init.data.PositionInfo
import com.playground.albazip.src.mypage.worker.init.data.UserInfo
import com.playground.albazip.src.mypage.worker.init.data.WorkInfo
import com.google.gson.annotations.SerializedName

data class ExistWorkerResponse (
    @SerializedName("data") val data: ExistWData,
) : BaseResponse()

data class ExistWData(
    @SerializedName("positionProfile") val positionProfile: PositionProfile,
    @SerializedName("workerInfo") val workerInfo: ExistWorkerInfo,
    @SerializedName("positionInfo") val positionInfo: PositionInfo,
    @SerializedName("positionTaskList") val positionTaskList: ArrayList<PositionTaskList>,
)

data class ExistWorkerInfo(
    @SerializedName("userInfo") val userInfo: UserInfo,
    @SerializedName("workInfo") val workInfo: WorkInfo,
    @SerializedName("joinDate") val joinDate: String,
)

data class PositionProfile(
    @SerializedName("rank") val rank: String,
    @SerializedName("title") val title: String,
    @SerializedName("imagePath") val imagePath: String,
    @SerializedName("firstName") val firstName: String
)

data class PositionTaskList(
    @SerializedName("id") val id: Int,
    @SerializedName("writerTitle") val writerTitle: String,
    @SerializedName("title") val title: String,
    @SerializedName("content") val content: String,
    @SerializedName("registerDate") val registerDate: String,
    @SerializedName("writerName") val writerName: String
)