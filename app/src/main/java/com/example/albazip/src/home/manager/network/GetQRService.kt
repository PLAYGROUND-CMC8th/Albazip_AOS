package com.example.albazip.src.home.manager.network

import com.example.albazip.config.ApplicationClass
import com.example.albazip.config.BaseResponse
import com.example.albazip.src.withdraw.network.WithDrawFragmentView
import com.example.albazip.src.withdraw.network.WithDrawRetrofitInterface
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header

class GetQRService(val view: GetQRFragmentView) {

    fun tryGetQRImg() {
        val getQRRetrofitInterface =
            ApplicationClass.sRetrofit.create(GetQRRetrofitInterface::class.java)
        val token = ApplicationClass.prefs.getString("X-ACCESS-TOKEN", "0")
        getQRRetrofitInterface.getQRImg(token).enqueue(object :
            Callback<ResponseBody> {
            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>
            ) {
                view.onGetQRSuccess(response.body() as ResponseBody)
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                view.onGetQRFailure(t.message ?: "통신 오류")
            }
        })
    }
}

interface GetQRRetrofitInterface {
    @GET("/home/qrcode")
    fun getQRImg(
        @Header("token") token: String,
    ): Call<ResponseBody>
}

interface GetQRFragmentView {

    fun onGetQRSuccess(response: ResponseBody)

    fun onGetQRFailure(message: String)
}