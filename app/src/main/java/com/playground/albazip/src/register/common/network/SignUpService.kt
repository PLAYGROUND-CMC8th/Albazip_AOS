package com.playground.albazip.src.register.common.network

import com.playground.albazip.config.ApplicationClass
import com.playground.albazip.src.register.common.data.remote.PostSignUpRequest
import com.playground.albazip.src.register.common.data.remote.SignUpResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignUpService (val view: SignUpFragmentView){

    fun tryPostSignUp(postSignUpRequest: PostSignUpRequest){
        val signUpRetrofitInterface = ApplicationClass.sRetrofit.create(SignUpRetrofitInterface::class.java)
        signUpRetrofitInterface.postSignUp(postSignUpRequest).enqueue(object :
            Callback<SignUpResponse> {
            override fun onResponse(call: Call<SignUpResponse>, response: Response<SignUpResponse>) {
                view.onPostSignUpSuccess(response.body() as SignUpResponse)
            }

            override fun onFailure(call: Call<SignUpResponse>, t: Throwable) {
                view.onPostSignUpFailure(t.message ?: "통신 오류")
            }
        })
    }

}