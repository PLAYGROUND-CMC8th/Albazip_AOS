package com.example.albazip.src.mypage.manager.workerlist.network

import com.example.albazip.config.ApplicationClass
import com.example.albazip.config.BaseResponse
import com.example.albazip.src.mypage.manager.workerlist.data.remote.PostAddWorkerRequest
import com.example.albazip.src.register.manager.data.remote.BNameResponse
import com.example.albazip.src.register.manager.network.BNameFragmentView
import com.example.albazip.src.register.manager.network.BNameRetrofitInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddWorkerService(val view: AddWorkerFragmentView) {

    fun tryPostWorker(postAddWorkerRequest:PostAddWorkerRequest){
        val addWorkerInterface = ApplicationClass.sRetrofit.create(AddWorkerRetrofitInterface::class.java)
        val token = ApplicationClass.prefs.getString("X-ACCESS-TOKEN","0")
        addWorkerInterface.postPosition(token,postAddWorkerRequest).enqueue(object :
            Callback<BaseResponse> {
            override fun onResponse(call: Call<BaseResponse>, response: Response<BaseResponse>) {
                view.onPostSuccess(response.body() as BaseResponse)
            }

            override fun onFailure(call: Call<BaseResponse>, t: Throwable) {
                view.onPostFailure(t.message ?: "통신 오류")
            }

        })
    }
}

interface AddWorkerFragmentView {

    fun onPostSuccess(response: BaseResponse)

    fun onPostFailure(message: String)
}