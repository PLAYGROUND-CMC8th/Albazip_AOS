package com.playground.albazip.src.mypage.common.profile.network

import com.playground.albazip.config.ApplicationClass
import com.playground.albazip.config.BaseResponse
import com.playground.albazip.src.mypage.common.profile.data.DefaultImgRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

class DefaultImgService(val view: DefaultImgFragmentView) {

    fun tryPostNewPW(defaultImgRequest: DefaultImgRequest){
        val defaultImgRetrofitInterface = ApplicationClass.sRetrofit.create(DefaultImgRetrofitInterface::class.java)
        val token = ApplicationClass.prefs.getString("X-ACCESS-TOKEN","0")
        defaultImgRetrofitInterface.postDefaultImg(token,defaultImgRequest).enqueue(object :
            Callback<BaseResponse> {
            override fun onResponse(call: Call<BaseResponse>, response: Response<BaseResponse>) {
                view.onDefaultImgPostSuccess(response.body() as BaseResponse)
            }

            override fun onFailure(call: Call<BaseResponse>, t: Throwable) {
                view.onDefaultImgFailure(t.message ?: "통신 오류")
            }
        })
    }
}

interface DefaultImgRetrofitInterface {
    @POST("/mypage/profile/image")
    fun postDefaultImg(@Header("token")token:String, @Body params: DefaultImgRequest): Call<BaseResponse>
}

interface DefaultImgFragmentView {

    fun onDefaultImgPostSuccess(response: BaseResponse)

    fun onDefaultImgFailure(message: String)
}
