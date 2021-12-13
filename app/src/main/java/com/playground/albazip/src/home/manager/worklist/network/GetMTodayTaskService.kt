package com.playground.albazip.src.home.manager.worklist.network

import com.playground.albazip.config.ApplicationClass
import com.playground.albazip.config.BaseResponse
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

class GetMTodayTaskService(val view: GetMTodayTaskFragmentView) {

    fun tryGetAllWHomeInfo(){
        val getMTodayTaskRetrofitInterface = ApplicationClass.sRetrofit.create(
            GetMTodayTaskRetrofitInterface::class.java)
        val token = ApplicationClass.prefs.getString("X-ACCESS-TOKEN","0")
        getMTodayTaskRetrofitInterface.getMTodayTask(token).enqueue(object :
            Callback<GetMTodayTaskResponse> {
            override fun onResponse(call: Call<GetMTodayTaskResponse>, response: Response<GetMTodayTaskResponse>) {
                view.onGetMTaskSuccess(response.body() as GetMTodayTaskResponse)
            }

            override fun onFailure(call: Call<GetMTodayTaskResponse>, t: Throwable) {
                view.onGetMTaskFailure(t.message ?: "통신 오류")
            }
        })
    }
}

interface GetMTodayTaskRetrofitInterface {
    @GET("/home/todayTask/manager")
    fun getMTodayTask(@Header("token")token:String): Call<GetMTodayTaskResponse>
}

interface GetMTodayTaskFragmentView {

    fun onGetMTaskSuccess(response: GetMTodayTaskResponse)

    fun onGetMTaskFailure(message: String)
}

data class GetMTodayTaskResponse(
    @SerializedName("data")val data:MTodayTaskResult
):BaseResponse()

data class MTodayTaskResult(
    @SerializedName("coTask")val coTask:MCoTask,
    @SerializedName("perTask")val perTask:ArrayList<MPerTask>
)

data class MCoTask(
    @SerializedName("nonComCoTask")val nonComCoTask:ArrayList<NonComCoTask>,
    @SerializedName("comWorker")val comWorker:ComWorker,
    @SerializedName("comCoTask")val comCoTask:ArrayList<ComCoTask>,
)

data class NonComCoTask(
    @SerializedName("taskId")val taskId:Int,
    @SerializedName("takTitle")val takTitle:String,
    @SerializedName("taskContent")val taskContent:String,
    @SerializedName("writerTitle")val writerTitle:String,
    @SerializedName("writerName")val writerName:String,
    @SerializedName("registerDate")val registerDate:String,
)

data class ComWorker(
    @SerializedName("comWorkerNum")val comWorkerNum:Int,
    @SerializedName("comWorker")val comWorker:ArrayList<InnerCoWorker>,
)

data class InnerCoWorker(
    @SerializedName("worker")val worker:String,
    @SerializedName("count")val count:Int,
    @SerializedName("image")val image:String?,
    @SerializedName("taskId")val taskId:ArrayList<Int>
)

data class ComCoTask(
    @SerializedName("taskId")val taskId:Int,
    @SerializedName("takTitle")val takTitle:String,
    @SerializedName("completeTime")val completeTime:String,
)

data class MPerTask(
    @SerializedName("workerId")val workerId:Int,
    @SerializedName("workerTitle")val workerTitle:String,
    @SerializedName("workerName")val workerName:String,
    @SerializedName("totalCount")val totalCount:Int,
    @SerializedName("completeCount")val completeCount:Int,
)