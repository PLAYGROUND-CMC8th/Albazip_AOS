package com.example.albazip.src.mypage.manager.workerlist.editposition.network

import com.example.albazip.config.ApplicationClass
import com.example.albazip.config.BaseResponse
import com.example.albazip.src.mypage.manager.workerlist.data.remote.PostAddWorkerRequest
import com.example.albazip.src.mypage.manager.workerlist.data.remote.TaskLists
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.*

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

data class PostEditWorkerRequest (
    @SerializedName("rank") val rank : String,
    @SerializedName("title") val title : String,
    @SerializedName("startTime") val startTime : String,
    @SerializedName("endTime") val endTime : String,
    @SerializedName("workDay") val workDay : ArrayList<String>,
    @SerializedName("breakTime") val breakTime : String,
    @SerializedName("salary") val salary : String,
    @SerializedName("salaryType") val salaryType : String,
    @SerializedName("taskList") val taskList : ArrayList<PostEditTaskLists>?
)

data class PostEditTaskLists(
    @SerializedName("id") val id : Int?,
    @SerializedName("title") val title : String,
    @SerializedName("content") val content : String?
)