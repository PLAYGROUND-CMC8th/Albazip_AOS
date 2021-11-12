package com.example.albazip.src.mypage.worker.init.data

import com.example.albazip.config.BaseResponse
import com.google.gson.annotations.SerializedName

data class GetWMyPageInfoResponse(
    @SerializedName("data") val data: WMyPageData

) : BaseResponse()

data class WMyPageData(
    @SerializedName("profileInfo") val profileInfo: ProfileInfo,
    @SerializedName("myInfo") val myInfo: MyInfo,
    @SerializedName("positionInfo") val positionInfo: PositionInfo,
    @SerializedName("boardInfo") val boardInfo: WBoardInfo
    )

/// 프로필 정보
data class ProfileInfo(
    @SerializedName("shopName") val shopName: String,
    @SerializedName("jobTitle") val jobTitle: String,
    @SerializedName("lastName") val lastName: String,
    @SerializedName("firstName") val firstName: String,
    @SerializedName("imagePath") val imagePath: String,
)

/////////////// MyInfo
data class MyInfo(
    @SerializedName("userInfo") val userInfo: UserInfo,
    @SerializedName("workInfo") val workInfo: WorkInfo,
    @SerializedName("joinDate") val joinDate: String,
)

data class UserInfo(
    @SerializedName("phone") val phone: String,
    @SerializedName("birthyear") val birthyear: String,
    @SerializedName("gender") val gender: Int
)

data class WorkInfo(
    @SerializedName("lateCount") val lateCount: Int,
    @SerializedName("coTaskCount") val coTaskCount: Int,
    @SerializedName("completeTaskCount") val completeTaskCount: Int,
    @SerializedName("totalTaskCount") val totalTaskCount: Int,
)


/////////////// PostInfo
data class PositionInfo(
    @SerializedName("startTime") val startTime: String,
    @SerializedName("endTime") val endTime: String,
    @SerializedName("workTime") val workTime: String,
    @SerializedName("workDay") val workDay: String,
    @SerializedName("breakTime") val breakTime: String,
    @SerializedName("salaryType") val salaryType: Int,
    @SerializedName("salary") val salary: Int,
)

////////////// WBoardInfo
data class WBoardInfo(
    @SerializedName("postInfo") val postInfo: ArrayList<WPostInfo>,
)

data class WPostInfo(
    @SerializedName("id") val id: Int,
    @SerializedName("writerJob") val writerJob: String,
    @SerializedName("writerName") val writerName: String,
    @SerializedName("title") val title: String,
    @SerializedName("content") val content: String,
    @SerializedName("commentCount") val commentCount: Int,
    @SerializedName("registerDate") val registerDate: String
)
