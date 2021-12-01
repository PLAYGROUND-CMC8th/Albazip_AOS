package com.playground.albazip.src.home.manager.editshop.network

import com.playground.albazip.config.ApplicationClass
import com.playground.albazip.config.BaseResponse
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

class GetEditShopInfoService(val view: GetEditShopInfoFragmentView) {

    fun tryGetEditShopInfo(positionId:Int){
        val getEditShopInfoRetrofitInterface = ApplicationClass.sRetrofit.create(
            GetEditShopInfoRetrofitInterface::class.java)
        val token = ApplicationClass.prefs.getString("X-ACCESS-TOKEN","0")
        getEditShopInfoRetrofitInterface.getEditShopInfo(token,positionId).enqueue(object :
            Callback<GetEditShopInfoResponse> {
            override fun onResponse(call: Call<GetEditShopInfoResponse>, response: Response<GetEditShopInfoResponse>) {
                view.onGetEditShopInfoSuccess(response.body() as GetEditShopInfoResponse)
            }

            override fun onFailure(call: Call<GetEditShopInfoResponse>, t: Throwable) {
                view.onGetEditShopInfoFailure(t.message ?: "통신 오류")
            }
        })
    }
}

interface GetEditShopInfoRetrofitInterface {
    @GET("/shop/{managerId}")
    fun getEditShopInfo(@Header("token")token:String,@Path(value = "managerId", encoded = false)positionId:Int): Call<GetEditShopInfoResponse>
}

interface GetEditShopInfoFragmentView {

    fun onGetEditShopInfoSuccess(response: GetEditShopInfoResponse)

    fun onGetEditShopInfoFailure(message: String)
}

data class GetEditShopInfoResponse(
    @SerializedName("data")val data:GetEditShopResult
):BaseResponse()

data class GetEditShopResult(
    @SerializedName("name")val name:String,
    @SerializedName("type")val type:String,
    @SerializedName("address")val address:String,
    @SerializedName("holiday")val holiday:ArrayList<String>,
    @SerializedName("startTime")val startTime:String,
    @SerializedName("endTime")val endTime:String,
    @SerializedName("payday")val payday:String,
)