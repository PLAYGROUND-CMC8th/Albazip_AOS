package com.example.albazip.src.home.worker.network

import com.example.albazip.config.ApplicationClass
import com.example.albazip.config.BaseResponse
import com.example.albazip.src.home.worker.data.GetAllWHomeResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Header
import retrofit2.http.PUT

class PutQRScanService(val view: PutQRScanFragmentView) {

    fun tryPutQRScan(){
        val putQRScanRetrofitInterface = ApplicationClass.sRetrofit.create(
            PutQRScanRetrofitInterface::class.java)
        val token = ApplicationClass.prefs.getString("X-ACCESS-TOKEN","0")
        putQRScanRetrofitInterface.putQRScan(token).enqueue(object :
            Callback<BaseResponse> {
            override fun onResponse(call: Call<BaseResponse>, response: Response<BaseResponse>) {
                view.onPutQRSuccess(response.body() as BaseResponse)
            }

            override fun onFailure(call: Call<BaseResponse>, t: Throwable) {
                view.onPutQRFailure(t.message ?: "통신 오류")
            }
        })
    }
}

interface PutQRScanRetrofitInterface {
    @PUT("/home/clock")
    fun putQRScan(@Header("token")token:String): Call<BaseResponse>
}

interface PutQRScanFragmentView {

    fun onPutQRSuccess(response: BaseResponse)

    fun onPutQRFailure(message: String)
}