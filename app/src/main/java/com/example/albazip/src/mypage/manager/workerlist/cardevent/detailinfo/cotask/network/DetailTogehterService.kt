package com.example.albazip.src.mypage.manager.workerlist.cardevent.detailinfo.cotask.network

import com.example.albazip.config.ApplicationClass
import com.example.albazip.config.BaseResponse
import com.example.albazip.src.mypage.common.workerdata.commute.data.GetCommuteInfoResponse
import com.example.albazip.src.mypage.manager.workerlist.cardevent.detailinfo.commute.network.DetailReCommuteFragmentView
import com.example.albazip.src.mypage.manager.workerlist.cardevent.detailinfo.commute.network.DetailReCommuteRetrofitInterface
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

class DetailTogehterService(val view: DetailReCommuteFragmentView) {

    fun tryDetailReGetCommuteInfo(positionId:Int) {
        val detailReCommuteRetrofitInterface =
            ApplicationClass.sRetrofit.create(DetailReCommuteRetrofitInterface::class.java)
        val token = ApplicationClass.prefs.getString("X-ACCESS-TOKEN", "0")
        detailReCommuteRetrofitInterface.getCommuteInfo(token,positionId).enqueue(object :
            Callback<GetCommuteInfoResponse> {
            override fun onResponse(
                call: Call<GetCommuteInfoResponse>,
                response: Response<GetCommuteInfoResponse>
            ) {
                view.onReCommuteGetSuccess(response.body() as GetCommuteInfoResponse)
            }

            override fun onFailure(call: Call<GetCommuteInfoResponse>, t: Throwable) {
                view.onReCommuteGetFailure(t.message ?: "통신 오류")
            }
        })
    }
}

interface DetailTogetherRetrofitInterface {
    @GET("/mypage/workers/{positionId}/workerInfo/coTaskInfoK")
    fun getDetailTogetherInfo(
        @Header("token") token: String,
        @Path(value = "positionId", encoded = false) positionId: Int,
    ): Call<GetCommuteInfoResponse>
}

interface DetailTogetherFragmentView {

    fun onDetailTogetherGetSuccess(response: GetCommuteInfoResponse)

    fun onDetailTogetherGetFailure(message: String)
}

data class GetDetailToResponse(
    @SerializedName("data")val data:ArrayList<DetailTOResultDate>,
):BaseResponse()


data class DetailTOResultDate(
    @SerializedName("y_2021_11_20", alternate = ["y_2021_11_14","y_2021_10_01","y_2021_11_01"])val data:ArrayList<DetailResultTask>
)

data class DetailResultTask(
   @SerializedName("title")val title:String,
   @SerializedName("content")val content:String?,
   @SerializedName("year")val year:String,
   @SerializedName("month")val month:String,
   @SerializedName("date")val date:String,
   @SerializedName("complete_date")val complete_date:String,
)



