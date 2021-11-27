package com.example.albazip.src.home.worker.opened.worklist.network

import com.example.albazip.config.ApplicationClass
import com.example.albazip.config.BaseResponse
import com.example.albazip.src.home.manager.worklist.network.GetMTodayTaskResponse
import com.example.albazip.src.home.manager.worklist.network.GetMTodayTaskRetrofitInterface
import com.example.albazip.src.home.manager.worklist.network.MCoTask
import com.example.albazip.src.home.manager.worklist.network.MTodayTaskResult
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

class GetWTodayTaskService(val view: GetWTodayTaskFragmentView) {

    fun tryGetWTodayTask(){
        val getWTodayTaskRetrofitInterface = ApplicationClass.sRetrofit.create(
            GetWTodayTaskRetrofitInterface::class.java)
        val token = ApplicationClass.prefs.getString("X-ACCESS-TOKEN","0")
        getWTodayTaskRetrofitInterface.getWTodayTask(token).enqueue(object :
            Callback<GetWTodayTaskResponse> {
            override fun onResponse(call: Call<GetWTodayTaskResponse>, response: Response<GetWTodayTaskResponse>) {
                view.onGetWTaskSuccess(response.body() as GetWTodayTaskResponse)
            }

            override fun onFailure(call: Call<GetWTodayTaskResponse>, t: Throwable) {
                view.onGetWTaskFailure(t.message ?: "통신 오류")
            }
        })
    }
}

interface GetWTodayTaskRetrofitInterface {
    @GET("/home/todayTask/worker")
    fun getWTodayTask(@Header("token")token:String): Call<GetWTodayTaskResponse>
}

interface GetWTodayTaskFragmentView {

    fun onGetWTaskSuccess(response: GetWTodayTaskResponse)

    fun onGetWTaskFailure(message: String)
}

data class GetWTodayTaskResponse(
    @SerializedName("data")val data: WTodayTaskResult
):BaseResponse()

data class WTodayTaskResult(
    @SerializedName("coTask")val coTask: MCoTask,
    @SerializedName("perTask")val perTask: WPerTask,
)

data class WPerTask(
    @SerializedName("positionTitle")val positionTitle: String,
    @SerializedName("nonComPerTask")val nonComPerTask: ArrayList<NonComPerTask>,
    @SerializedName("compPerTask")val compPerTask: ArrayList<ComPerTask>,
)

data class NonComPerTask(
    @SerializedName("taskId")val taskId: Int,
    @SerializedName("takTitle")val takTitle: String,
    @SerializedName("taskContent")val taskContent: String,
    @SerializedName("writerTitle")val writerTitle: String,
    @SerializedName("writerName")val writerName: String,
    @SerializedName("registerDate")val registerDate: String,
)

data class ComPerTask(
    @SerializedName("taskId")val taskId: Int,
    @SerializedName("takTitle")val takTitle: String,
    @SerializedName("completeTime")val completeTime: String,
)