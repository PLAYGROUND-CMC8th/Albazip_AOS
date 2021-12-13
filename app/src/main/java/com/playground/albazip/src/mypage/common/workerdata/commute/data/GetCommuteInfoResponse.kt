package com.playground.albazip.src.mypage.common.workerdata.commute.data

import com.playground.albazip.config.BaseResponse
import com.google.gson.annotations.SerializedName

data class GetCommuteInfoResponse(
    @SerializedName("data") val data: CommuteResult

) : BaseResponse()

data class CommuteResult(
    @SerializedName("year") val year:String,
    @SerializedName("month") val month:String,
    @SerializedName("commuteData") val commuteData:ArrayList<CommuteData>
)

data class CommuteData(
    @SerializedName("year") val year:String,
    @SerializedName("month") val month:String,
    @SerializedName("day") val day:String,
    @SerializedName("start_time") val start_time:String,
    @SerializedName("end_time") val end_time:String,
    @SerializedName("real_start_time") val real_start_time:String?,
    @SerializedName("real_end_time") val real_end_time:String?,
    @SerializedName("start_late") val start_late:Int,
    @SerializedName("end_late") val end_late:Int,
)