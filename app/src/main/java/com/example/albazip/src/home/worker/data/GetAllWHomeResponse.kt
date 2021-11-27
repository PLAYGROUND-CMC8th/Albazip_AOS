package com.example.albazip.src.home.worker.data

import com.example.albazip.config.BaseResponse
import com.example.albazip.src.home.manager.data.HomeShopInfo
import com.example.albazip.src.home.manager.data.HomeTaskInfo
import com.example.albazip.src.home.manager.data.HomeTodayInfo
import com.google.gson.annotations.SerializedName

data class GetAllWHomeResponse (
    @SerializedName("data")val data:AllHomeWResult
):BaseResponse()

data class AllHomeWResult(
    @SerializedName("todayInfo")val todayInfo: HomeTodayInfo,
    @SerializedName("shopInfo")val shopInfo: WHomeShopInfo,
    @SerializedName("taskInfo")val taskInfo: HomeTaskInfo,
    @SerializedName("scheduleInfo")val scheduleInfo: HomeScheduleInfo,
    @SerializedName("boardInfo")val boardInfo:ArrayList<WHomeBoardInfo>,
)

data class HomeScheduleInfo(
    @SerializedName("positionTitle")val positionTitle: String,
    @SerializedName("startTime")val startTime: String,
    @SerializedName("endTime")val endTime: String,
    @SerializedName("realStartTime")val realStartTime: String?,
    @SerializedName("realEndTime")val realEndTime: String?,
    @SerializedName("remainTime")val remainTime: String
)

data class WHomeBoardInfo(
    @SerializedName("status")val status:Int,
    @SerializedName("id")val id:Int,
    @SerializedName("title")val title:String,
    @SerializedName("confirm")val confirm:Int
)

data class WHomeShopInfo(
    @SerializedName("status")val status:Int,
    @SerializedName("shopName")val name:String,
)