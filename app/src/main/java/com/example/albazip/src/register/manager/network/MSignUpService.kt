package com.example.albazip.src.register.manager.network

import com.example.albazip.config.ApplicationClass
import com.example.albazip.config.ApplicationClass.Companion.prefs
import com.example.albazip.config.BaseResponse
import com.example.albazip.src.register.common.data.remote.PostSignUpRequest
import com.example.albazip.src.register.common.data.remote.SignUpResponse
import com.example.albazip.src.register.common.network.SignUpFragmentView
import com.example.albazip.src.register.common.network.SignUpRetrofitInterface
import com.example.albazip.src.register.manager.data.remote.PostMSignUpRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MSignUpService (val view: MSignUpFragmentView){

    fun tryPostMSignUp(postMSignUpRequest: PostMSignUpRequest){
        val mSignUpRetrofitInterface = ApplicationClass.sRetrofit.create(MSignUpRetrofitInterface::class.java)
        val token = prefs.getString("X-ACCESS-TOKEN","0")
        mSignUpRetrofitInterface.postSignUp(token,postMSignUpRequest).enqueue(object :
            Callback<BaseResponse> {
            override fun onResponse(call: Call<BaseResponse>, response: Response<BaseResponse>) {
                view.onPostMSignUpSuccess(response.body() as BaseResponse)
            }

            override fun onFailure(call: Call<BaseResponse>, t: Throwable) {
                view.onPostMSignUpFailure(t.message ?: "통신 오류")
            }

        })
    }

}

interface MSignUpFragmentView {

    fun onPostMSignUpSuccess(response: BaseResponse)

    fun onPostMSignUpFailure(message: String)
}