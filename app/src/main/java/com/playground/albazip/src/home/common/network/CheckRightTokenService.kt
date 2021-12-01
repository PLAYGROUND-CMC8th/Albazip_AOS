package com.playground.albazip.src.splash

import com.playground.albazip.config.ApplicationClass
import com.playground.albazip.config.BaseResponse
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

class CheckRightTokenService(val view: GetCheckRightTokenFragmentView) {

    fun tryGetCheckToken(){
        val getRightTokenRetrofitInterface = ApplicationClass.sRetrofit.create(
            GetRightTokenRetrofitInterface::class.java)
        val token = ApplicationClass.prefs.getString("X-ACCESS-TOKEN","0")
        getRightTokenRetrofitInterface.getCheckToken(token).enqueue(object :
            Callback<GetRightTokenResponse> {
            override fun onResponse(call: Call<GetRightTokenResponse>, response: Response<GetRightTokenResponse>) {
                view.onGetTokenSuccess(response.body() as GetRightTokenResponse)
            }

            override fun onFailure(call: Call<GetRightTokenResponse>, t: Throwable) {
                view.onGetTokenFailure(t.message ?: "통신 오류")
            }
        })
    }
}

interface GetRightTokenRetrofitInterface {
    @GET("/token")
    fun getCheckToken(@Header("token")token:String): Call<GetRightTokenResponse>
}

interface GetCheckRightTokenFragmentView {

    fun onGetTokenSuccess(response: GetRightTokenResponse)

    fun onGetTokenFailure(message: String)
}

data class GetRightTokenResponse(
    @SerializedName("status")val status:Int
):BaseResponse()