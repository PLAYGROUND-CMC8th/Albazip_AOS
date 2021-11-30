package com.playground.albazip.src.home.manager.editshop.network

import com.playground.albazip.config.ApplicationClass
import com.playground.albazip.config.BaseResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.Header
import retrofit2.http.Path

class DelShopService(val view: DelShopFragmentView) {

    fun tryDelShopInfo(positionId:Int){
        val delShopInfoRetrofitInterface = ApplicationClass.sRetrofit.create(
            DelShopInfoRetrofitInterface::class.java)
        val token = ApplicationClass.prefs.getString("X-ACCESS-TOKEN","0")
        delShopInfoRetrofitInterface.delShopInfo(token,positionId).enqueue(object :
            Callback<BaseResponse> {
            override fun onResponse(call: Call<BaseResponse>, response: Response<BaseResponse>) {
                view.onDelShopInfoSuccess(response.body() as BaseResponse)
            }

            override fun onFailure(call: Call<BaseResponse>, t: Throwable) {
                view.onDelShopInfoFailure(t.message ?: "통신 오류")
            }
        })
    }
}

interface DelShopInfoRetrofitInterface {
    @DELETE("/shop/{managerId}")
    fun delShopInfo(@Header("token")token:String, @Path(value = "managerId", encoded = false)positionId:Int): Call<BaseResponse>
}

interface DelShopFragmentView {

    fun onDelShopInfoSuccess(response: BaseResponse)

    fun onDelShopInfoFailure(message: String)
}