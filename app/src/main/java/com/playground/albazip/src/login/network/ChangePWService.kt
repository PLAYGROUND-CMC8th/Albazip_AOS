package com.playground.albazip.src.login.network
import com.playground.albazip.config.ApplicationClass
import com.playground.albazip.config.BaseResponse
import com.playground.albazip.src.login.data.PostNewPWRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

class ChangePWService(val view: ChangePWFragmentView) {

    fun tryPostNewPW(postNewPWRequest: PostNewPWRequest){
        val setNewPWRetrofitInterface = ApplicationClass.sRetrofit.create(SetNewPWRetrofitInterface::class.java)
        setNewPWRetrofitInterface.postNewPW(postNewPWRequest).enqueue(object :
            Callback<BaseResponse> {
            override fun onResponse(call: Call<BaseResponse>, response: Response<BaseResponse>) {
                view.onNewPWPostSuccess(response.body() as BaseResponse)
            }

            override fun onFailure(call: Call<BaseResponse>, t: Throwable) {
                view.onNewPWPostFailure(t.message ?: "통신 오류")
            }
        })
    }
}

interface SetNewPWRetrofitInterface {
    @POST("/user/signin/password")
    fun postNewPW(@Body params: PostNewPWRequest): Call<BaseResponse>
}

interface ChangePWFragmentView {

    fun onNewPWPostSuccess(response: BaseResponse)

    fun onNewPWPostFailure(message: String)
}