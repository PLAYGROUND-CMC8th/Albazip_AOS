package com.playground.albazip.src.home.manager.editshop.network

import com.playground.albazip.config.ApplicationClass
import com.playground.albazip.config.BaseResponse
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

class PostEditShopService(val view: PostEditShopInfoFragmentView) {

    fun tryPostEditShopInfo(positionId:Int,postEditShopRequest: PostEditShopRequest){
        val postEditShopInfoRetrofitInterface = ApplicationClass.sRetrofit.create(
            PostEditShopInfoRetrofitInterface::class.java)
        val token = ApplicationClass.prefs.getString("X-ACCESS-TOKEN","0")
        postEditShopInfoRetrofitInterface.postEditShopInfo(token,positionId,postEditShopRequest).enqueue(object :
            Callback<BaseResponse> {
            override fun onResponse(call: Call<BaseResponse>, response: Response<BaseResponse>) {
                view.onPostEditShopInfoSuccess(response.body() as BaseResponse)
            }

            override fun onFailure(call: Call<BaseResponse>, t: Throwable) {
                view.onPostEditShopInfoFailure(t.message ?: "통신 오류")
            }
        })
    }
}

interface PostEditShopInfoRetrofitInterface {
    @POST("/shop/{managerId}")
    fun postEditShopInfo(@Header("token")token:String, @Path(value = "managerId", encoded = false)positionId:Int,
                         @Body params: PostEditShopRequest
    ): Call<BaseResponse>
}

interface PostEditShopInfoFragmentView {

    fun onPostEditShopInfoSuccess(response: BaseResponse)

    fun onPostEditShopInfoFailure(message: String)
}

data class PostEditShopRequest(
    @SerializedName("name")val name:String,
    @SerializedName("type")val type:String,
    @SerializedName("address")val address:String,
    @SerializedName("holiday")val holiday:ArrayList<String>,
    @SerializedName("startTime")val startTime:String,
    @SerializedName("endTime")val endTime:String,
    @SerializedName("payday")val payday:String
)