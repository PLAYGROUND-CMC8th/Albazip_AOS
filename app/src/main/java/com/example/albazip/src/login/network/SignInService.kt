package com.example.albazip.src.login.network

import com.example.albazip.config.ApplicationClass
import com.example.albazip.src.login.data.PostSignInRequest
import com.example.albazip.src.login.data.SignInResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignInService(val view: SignInFragmentView)  {

    fun tryPostLogin(postSignInRequest: PostSignInRequest){
        val loginRetrofitInterface = ApplicationClass.sRetrofit.create(SignInRetrofitInterface::class.java)
        loginRetrofitInterface.postSignIn(postSignInRequest).enqueue(object :
            Callback<SignInResponse> {
            override fun onResponse(call: Call<SignInResponse>, response: Response<SignInResponse>) {
                view.onPostSingInSuccess(response.body() as SignInResponse)
            }

            override fun onFailure(call: Call<SignInResponse>, t: Throwable) {
                view.onPostSignInFailure(t.message ?: "통신 오류")
            }
        })
    }
}

interface SignInFragmentView {

    fun onPostSingInSuccess(response: SignInResponse)

    fun onPostSignInFailure(message: String)
}