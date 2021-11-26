package com.example.albazip.src.mypage.manager.workerlist.editposition.network

import com.example.albazip.config.ApplicationClass
import com.example.albazip.config.BaseResponse
import com.example.albazip.src.mypage.manager.workerlist.data.remote.TaskLists
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

class GetPositionInfoService(val view: GetPositionInfoFragmentView) {

    fun tryGetPositionInfo(positionId: Int) {
        val getPositionInfoRetrofitInterface =
            ApplicationClass.sRetrofit.create(GetPositionInfoRetrofitInterface::class.java)
        val token = ApplicationClass.prefs.getString("X-ACCESS-TOKEN", "0")
        getPositionInfoRetrofitInterface.getPositionInfo(token,positionId).enqueue(object :
            Callback<GetPositionInfoResponse> {
            override fun onResponse(
                call: Call<GetPositionInfoResponse>,
                response: Response<GetPositionInfoResponse>
            ) {
                view.onGetPositionInfoSuccess(response.body() as GetPositionInfoResponse)
            }

            override fun onFailure(call: Call<GetPositionInfoResponse>, t: Throwable) {
                view.onGetPositionInfoFailure(t.message ?: "통신 오류")
            }
        })
    }
}

interface GetPositionInfoRetrofitInterface {
    @GET("/position/{positionId}")
    fun getPositionInfo(
        @Header("token") token: String,
        @Path(value = "positionId", encoded = false)positionId:Int
    ): Call<GetPositionInfoResponse>
}

interface GetPositionInfoFragmentView {

    fun onGetPositionInfoSuccess(response: GetPositionInfoResponse)

    fun onGetPositionInfoFailure(message: String)
}

data class GetPositionInfoResponse(
    @SerializedName("data") val data: EditPositionInfoData,
):BaseResponse()

data class EditPositionInfoData(
    @SerializedName("rank") val rank: String, // 알바생
    @SerializedName("title") val title: String, // 평일미들
    @SerializedName("workDay") val workDay:ArrayList<String>, // 근무요일
    @SerializedName("startTime") val startTime: String, // 오픈시간
    @SerializedName("endTime") val endTime: String, // 마감시간
    @SerializedName("breakTime") val breakTime: String, // 쉬는시간
    @SerializedName("salaryType") val salaryType: Int, // 페이타입
    @SerializedName("salary") val salary: String, // 페이
    @SerializedName("taskList") val taskList: ArrayList<EditTaskLists>, // 페이
)

data class EditTaskLists(
    @SerializedName("id") val id : Int,
    @SerializedName("title") val title : String,
    @SerializedName("content") val content : String
)