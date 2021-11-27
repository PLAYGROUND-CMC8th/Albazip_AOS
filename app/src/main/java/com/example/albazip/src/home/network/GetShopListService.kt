package com.example.albazip.src.home.network

import com.example.albazip.config.ApplicationClass
import com.example.albazip.config.BaseResponse
import com.example.albazip.src.mypage.common.workerdata.cotask.data.GetCoTaskInfoResponse
import com.example.albazip.src.mypage.common.workerdata.cotask.network.CoTaskFragmentView
import com.example.albazip.src.mypage.common.workerdata.cotask.network.CoTaskRetrofitInterface
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

class GetShopListService(val view: ShopListFragmentView) {

    fun tryGetShopList(){
        val shopListRetrofitInterface = ApplicationClass.sRetrofit.create(ShopListRetrofitInterface::class.java)
        val token = ApplicationClass.prefs.getString("X-ACCESS-TOKEN","0")
        shopListRetrofitInterface.getShopList(token).enqueue(object :
            Callback<ShopListResponse> {
            override fun onResponse(call: Call<ShopListResponse>, response: Response<ShopListResponse>) {
                view.onShopListGetSuccess(response.body() as ShopListResponse)
            }

            override fun onFailure(call: Call<ShopListResponse>, t: Throwable) {
                view.onShopListGetFailure(t.message ?: "통신 오류")
            }
        })
    }
}

interface ShopListRetrofitInterface {
    @GET("/home/shopList")
    fun getShopList(@Header("token")token:String): Call<ShopListResponse>
}

interface ShopListFragmentView {

    fun onShopListGetSuccess(response: ShopListResponse)

    fun onShopListGetFailure(message: String)
}

data class ShopListResponse(
    @SerializedName("data")val data:ArrayList<ShopListResult>
):BaseResponse()

data class ShopListResult(
    @SerializedName("managerId")val managerId:Int,
    @SerializedName("workerId")val workerId:Int,
    @SerializedName("shop_name")val shop_name:String,
    @SerializedName("status")val status:Int,

)