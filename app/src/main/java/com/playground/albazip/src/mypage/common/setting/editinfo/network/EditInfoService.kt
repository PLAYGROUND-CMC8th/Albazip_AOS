package com.playground.albazip.src.mypage.common.setting.editinfo.network

import com.playground.albazip.config.ApplicationClass
import com.playground.albazip.config.BaseResponse
import com.playground.albazip.src.mypage.common.setting.editinfo.data.EditInfoRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

class EditInfoService(val view: EditInfoFragmentView) {

    fun tryEditInfo(editInfoRequest: EditInfoRequest){
        val editInfoRetrofitInterface = ApplicationClass.sRetrofit.create(EditInfoRetrofitInterface::class.java)
        val token = ApplicationClass.prefs.getString("X-ACCESS-TOKEN","0")
        editInfoRetrofitInterface.postMyInfo(token,editInfoRequest).enqueue(object :
            Callback<BaseResponse> {
            override fun onResponse(call: Call<BaseResponse>, response: Response<BaseResponse>) {
                view.onEditInfoPostSuccess(response.body() as BaseResponse)
            }

            override fun onFailure(call: Call<BaseResponse>, t: Throwable) {
                view.onEditInfoPostFailure(t.message ?: "통신 오류")
            }
        })
    }
}

interface EditInfoRetrofitInterface {
    @POST("/mypage/setting/myinfo")
    fun postMyInfo(@Header("token")token:String,@Body params: EditInfoRequest): Call<BaseResponse>
}

interface EditInfoFragmentView {

    fun onEditInfoPostSuccess(response: BaseResponse)

    fun onEditInfoPostFailure(message: String)
}