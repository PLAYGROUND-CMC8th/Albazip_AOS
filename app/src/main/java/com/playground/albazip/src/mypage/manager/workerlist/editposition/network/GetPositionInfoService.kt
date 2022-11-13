package com.playground.albazip.src.mypage.manager.workerlist.editposition.network

import com.playground.albazip.config.ApplicationClass
import com.playground.albazip.config.BaseResponse
import com.google.gson.annotations.SerializedName
import com.playground.albazip.src.mypage.worker.init.data.PositionInfo
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
        getPositionInfoRetrofitInterface.getPositionInfo(token, positionId).enqueue(object :
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
        @Path(value = "positionId", encoded = false) positionId: Int
    ): Call<GetPositionInfoResponse>
}

interface GetPositionInfoFragmentView {

    fun onGetPositionInfoSuccess(response: GetPositionInfoResponse)

    fun onGetPositionInfoFailure(message: String)
}

data class GetPositionInfoResponse(
    @SerializedName("data") val data: EditPositionInfoData,
) : BaseResponse()

data class EditPositionInfoData(
    val title: String,
    val breakTime: String,
    val salary: String,
    val salaryType: Int,
    val workSchedule: ArrayList<WorkSchedule>,
    val taskList: ArrayList<EditTaskLists>, // 페이
) {
    data class WorkSchedule(
        val day: String,
        val endTime: String,
        val startTime: String
    )
}

data class EditTaskLists(
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String,
    @SerializedName("content") val content: String
)