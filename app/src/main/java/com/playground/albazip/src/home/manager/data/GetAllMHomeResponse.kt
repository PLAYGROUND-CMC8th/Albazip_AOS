package com.playground.albazip.src.home.manager.data

import com.playground.albazip.config.BaseResponse
import com.google.gson.annotations.SerializedName

data class GetAllMHomeResponse(
    @SerializedName("data") val data: AllHomeMResult,
): BaseResponse()

data class AllHomeMResult(
    @SerializedName("todayInfo")val todayInfo:HomeTodayInfo,
    @SerializedName("shopInfo")val shopInfo:HomeShopInfo,
    @SerializedName("workerInfo")val workerInfo:ArrayList<HomeWorkerInfo>,
    @SerializedName("taskInfo")val taskInfo:HomeTaskInfo,
    @SerializedName("boardInfo")val boardInfo:ArrayList<HomeBoardInfo>,
)

data class HomeTodayInfo(
    @SerializedName("month")val month:Int,
    @SerializedName("date")val date:Int,
    @SerializedName("day")val day:String
)

data class HomeShopInfo(
    @SerializedName("status")val status:Int,
    @SerializedName("name")val name:String,
    @SerializedName("startTime")val startTime:String,
    @SerializedName("endTime")val endTime:String
)

data class HomeWorkerInfo(
    @SerializedName("title")val title:String,
    @SerializedName("firstName")val firstName:String
)

///////////// 업무 목록 /////////////////////////
data class HomeTaskInfo(
    @SerializedName("coTask")val coTask:HomeCoTask,
    @SerializedName("perTask")val perTask:HomePerTask,
)
data class HomeCoTask(
    @SerializedName("completeCount")val completeCount:Int,
    @SerializedName("totalCount")val totalCount:Int
)
data class HomePerTask(
    @SerializedName("completeCount")val completeCount:Int,
    @SerializedName("totalCount")val totalCount:Int
)


data class HomeBoardInfo(
    @SerializedName("status")val status:Int,
    @SerializedName("id")val id:Int,
    @SerializedName("title")val title:String
)