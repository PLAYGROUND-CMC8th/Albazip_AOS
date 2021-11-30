package com.playground.albazip.src.mypage.common.setting.editinfo.network

import com.playground.albazip.config.ApplicationClass
import com.playground.albazip.config.BaseResponse
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

class EditPhoneNumService(val view: EditPhoneNumFragmentView) {

    fun tryEditInfo(editPhoneNumRequest: EditPhoneNumRequest){
        val editPhoneNumRetrofitInterface = ApplicationClass.sRetrofit.create(EditPhoneNumRetrofitInterface::class.java)
        val token = ApplicationClass.prefs.getString("X-ACCESS-TOKEN","0")
        editPhoneNumRetrofitInterface.postEditPhoneNum(token,editPhoneNumRequest).enqueue(object :
            Callback<BaseResponse> {
            override fun onResponse(call: Call<BaseResponse>, response: Response<BaseResponse>) {
                view.onEditPhoneNumPostSuccess(response.body() as BaseResponse)
            }

            override fun onFailure(call: Call<BaseResponse>, t: Throwable) {
                view.onEditPhoneNumFailure(t.message ?: "통신 오류")
            }
        })
    }
}

interface EditPhoneNumRetrofitInterface {
    @POST("/mypage/setting/myinfo/phone")
    fun postEditPhoneNum(@Header("token")token:String, @Body params: EditPhoneNumRequest): Call<BaseResponse>
}

interface EditPhoneNumFragmentView {

    fun onEditPhoneNumPostSuccess(response: BaseResponse)

    fun onEditPhoneNumFailure(message: String)
}

data class EditPhoneNumRequest(@SerializedName("phone") val phone: String)