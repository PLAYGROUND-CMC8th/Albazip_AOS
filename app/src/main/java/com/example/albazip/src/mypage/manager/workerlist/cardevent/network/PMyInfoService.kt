package com.example.albazip.src.mypage.manager.workerlist.cardevent.network

import com.example.albazip.config.ApplicationClass
import com.example.albazip.config.BaseResponse
import com.example.albazip.src.mypage.worker.init.data.UserInfo
import com.example.albazip.src.mypage.worker.init.data.WorkInfo
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

class PMyInfoService(val view: PMyInfoFragmentView) {
    fun tryGetExistCard(positionId: Int){
        val pMyInfoRetrofitInterface = ApplicationClass.sRetrofit.create(PMyInfoRetrofitInterface::class.java)
        val token = ApplicationClass.prefs.getString("X-ACCESS-TOKEN","0")
        pMyInfoRetrofitInterface.getPMyInfo(token,positionId).enqueue(object :
            Callback<PMyInfoResponse> {
            override fun onResponse(
                call: Call<PMyInfoResponse>,
                response: Response<PMyInfoResponse>
            ) {
                view.onGetMyInfoSuccess(response.body() as PMyInfoResponse)
            }

            override fun onFailure(call: Call<PMyInfoResponse>, t: Throwable) {
                view.onGetMyInfoFailure(t.message ?: "통신 오류")
            }

        })
    }
}


interface PMyInfoRetrofitInterface {
    @GET("/mypage/workers/{positionId}/workerInfo")
    fun getPMyInfo(@Header("token")token:String, @Path(value = "positionId", encoded = false)positionId:Int): Call<PMyInfoResponse>
}

interface PMyInfoFragmentView {

    fun onGetMyInfoSuccess(response: PMyInfoResponse)

    fun onGetMyInfoFailure(message: String)
}

data class PMyInfoResponse(
    @SerializedName("data") val data : PMyInfoData,
): BaseResponse()

data class PMyInfoData(
    @SerializedName("userInfo") val userInfo : UserInfo,
    @SerializedName("workInfo") val workInfo : WorkInfo,
    @SerializedName("joinDate") val joinDate : String,
)