package com.playground.albazip.src.register.worker.network

import com.playground.albazip.config.ApplicationClass
import com.playground.albazip.config.ApplicationClass.Companion.prefs
import com.playground.albazip.src.register.common.data.remote.PositionRegisterResponse
import com.playground.albazip.src.register.worker.data.PostSignInWorkerRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

class WSignUpService(val view: WSignUpFragmentView) {

    fun tryPostNewPW(postSignInWorkerRequest: PostSignInWorkerRequest){
        val wSignUpRetrofitInterface = ApplicationClass.sRetrofit.create(WSignUpRetrofitInterface::class.java)
        val token = prefs.getString("X-ACCESS-TOKEN","0")
        wSignUpRetrofitInterface.postWSignUp(token,postSignInWorkerRequest).enqueue(object :
            Callback<PositionRegisterResponse> {
            override fun onResponse(call: Call<PositionRegisterResponse>, response: Response<PositionRegisterResponse>) {
                view.onWorkerPostSuccess(response.body() as PositionRegisterResponse)
            }

            override fun onFailure(call: Call<PositionRegisterResponse>, t: Throwable) {
                view.onWorkerPostFailure(t.message ?: "통신 오류")
            }
        })
    }
}

interface WSignUpRetrofitInterface {
    @POST("/user/signup/worker")
    fun postWSignUp(@Header("token")token:String, @Body params: PostSignInWorkerRequest): Call<PositionRegisterResponse>
}

interface WSignUpFragmentView {

    fun onWorkerPostSuccess(response: PositionRegisterResponse)

    fun onWorkerPostFailure(message: String)
}
