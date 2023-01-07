package com.playground.albazip.src.mypage.manager.workerlist.editposition.network

import com.playground.albazip.config.ApplicationClass
import com.playground.albazip.config.BaseResponse
import com.google.gson.annotations.SerializedName
import com.playground.albazip.src.mypage.manager.workerlist.data.local.EditTodoData
import com.playground.albazip.src.update.setworker.network.RequestAddPosition
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.*
import java.io.Serializable

class PostPositionInfoService(val view: PostPositionInfoFragmentView) {

    fun tryPostPositionInfo(positionId: Int,postEditWorkerRequest: PostEditWorkerRequest) {
        val postPositionInfoRetrofitInterface =
            ApplicationClass.sRetrofit.create(PostPositionInfoRetrofitInterface::class.java)
        val token = ApplicationClass.prefs.getString("X-ACCESS-TOKEN", "0")
        postPositionInfoRetrofitInterface.postPositionInfo(token,positionId,postEditWorkerRequest).enqueue(object :
            Callback<BaseResponse> {
            override fun onResponse(
                call: Call<BaseResponse>,
                response: Response<BaseResponse>
            ) {
                view.onPostPositionInfoSuccess(response.body() as BaseResponse)
            }

            override fun onFailure(call: Call<BaseResponse>, t: Throwable) {
                view.onPostPositionInfoFailure(t.message ?: "통신 오류")
            }
        })
    }
}

interface PostPositionInfoRetrofitInterface {
    @POST("/position/{positionId}")
    fun postPositionInfo(
        @Header("token") token: String,
        @Path(value = "positionId", encoded = false)positionId:Int,
        @Body params: PostEditWorkerRequest
    ): Call<BaseResponse>
}

interface PostPositionInfoFragmentView {

    fun onPostPositionInfoSuccess(response: BaseResponse)

    fun onPostPositionInfoFailure(message: String)
}

data class PostEditWorkerRequest(
    val breakTime: String,
    val rank: String,
    val salary: String,
    val salaryType: Int,
    val taskList: ArrayList<EditTodoData>?,
    val title: String,
    val workSchedule: ArrayList<RequestAddPosition.WorkSchedule>
){
    data class WorkSchedule(
        val day: String,
        val startTime: String,
        val endTime: String,
    ): Serializable
}

data class PostEditTaskLists(
    @SerializedName("id") val id : Int?,
    @SerializedName("title") val title : String,
    @SerializedName("content") val content : String?
)