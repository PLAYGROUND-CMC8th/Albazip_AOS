package com.example.albazip.src.mypage.manager.workerlist.cardevent.network

import com.example.albazip.config.ApplicationClass
import com.example.albazip.config.BaseResponse
import com.example.albazip.src.mypage.manager.workerlist.editposition.network.GetPositionInfoResponse
import com.example.albazip.src.mypage.worker.init.data.PositionInfo
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

class PPositionInfoService(val view: PPositionInfoFragmentView) {
    fun tryGetPositionInfo(positionId: Int){
        val pPositionInfoRetrofitInterface = ApplicationClass.sRetrofit.create(PPositionInfoRetrofitInterface::class.java)
        val token = ApplicationClass.prefs.getString("X-ACCESS-TOKEN","0")
        pPositionInfoRetrofitInterface.getPPositionInfo(token,positionId).enqueue(object :
            Callback<GetPPositionInfoResponse> {
            override fun onResponse(
                call: Call<GetPPositionInfoResponse>,
                response: Response<GetPPositionInfoResponse>
            ) {
                view.onGetPositionInfoSuccess(response.body() as GetPPositionInfoResponse)
            }

            override fun onFailure(call: Call<GetPPositionInfoResponse>, t: Throwable) {
                view.onGetPositionInfoFailure(t.message ?: "통신 오류")
            }

        })
    }
}


interface PPositionInfoRetrofitInterface {
    @GET("/mypage/workers/{positionId}/positionInfo")
    fun getPPositionInfo(@Header("token")token:String, @Path(value = "positionId", encoded = false)positionId:Int): Call<GetPPositionInfoResponse>
}

interface PPositionInfoFragmentView {

    fun onGetPositionInfoSuccess(response: GetPPositionInfoResponse)

    fun onGetPositionInfoFailure(message: String)
}

data class GetPPositionInfoResponse(
    @SerializedName("data") val data: PositionInfo,
):BaseResponse()