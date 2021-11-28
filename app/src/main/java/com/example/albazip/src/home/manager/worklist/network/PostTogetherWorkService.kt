package com.example.albazip.src.home.manager.worklist.network

import com.example.albazip.config.ApplicationClass
import com.example.albazip.config.BaseResponse
import com.example.albazip.src.home.worker.opened.worklist.network.GetWPerTaskResponse
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

class PostTogetherWorkService(val view: PostCoWorkFragmentView) {

    fun tryPostCoWorkInfo(postCoWorkRequest: PostCoWorkRequest){
        val postCoWorkRetrofitInterface = ApplicationClass.sRetrofit.create(
            PostCoWorkRetrofitInterface::class.java)
        val token = ApplicationClass.prefs.getString("X-ACCESS-TOKEN","0")
        postCoWorkRetrofitInterface.postCoWork(token,postCoWorkRequest).enqueue(object :
            Callback<BaseResponse> {
            override fun onResponse(call: Call<BaseResponse>, response: Response<BaseResponse>) {
                view.onPostWorkSuccess(response.body() as BaseResponse)
            }

            override fun onFailure(call: Call<BaseResponse>, t: Throwable) {
                view.onPostWorkFailure(t.message ?: "통신 오류")
            }
        })
    }
}

interface PostCoWorkRetrofitInterface {
    @POST("/home/todayTask/coTask")
    fun postCoWork(@Header("token")token:String, @Body params:PostCoWorkRequest): Call<BaseResponse>
}

interface PostCoWorkFragmentView {

    fun onPostWorkSuccess(response: BaseResponse)

    fun onPostWorkFailure(message: String)
}

data class PostCoWorkRequest(
    @SerializedName("coTaskList")val coTaskList:ArrayList<PostCoTask>
)
data class PostCoTask(
    @SerializedName("title")val title:String,
    @SerializedName("content")val content:String?,
)