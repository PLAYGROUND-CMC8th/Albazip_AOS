package com.playground.albazip.src.mypage.common.profile.network

import com.playground.albazip.config.ApplicationClass
import com.playground.albazip.config.BaseResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.*

class GalleryImgService(val view: GalleryImgFragmentView) {

    fun tryPostGalleryImg(uploadImage: MultipartBody.Part?){
        val galleryImgRetrofitInterface = ApplicationClass.sRetrofit.create(GalleryImgRetrofitInterface::class.java)
        val token = ApplicationClass.prefs.getString("X-ACCESS-TOKEN","0")
        galleryImgRetrofitInterface.postGalleryImg(token,uploadImage).enqueue(object :
            Callback<BaseResponse> {
            override fun onResponse(call: Call<BaseResponse>, response: Response<BaseResponse>) {
                view.onGalleryImgPostSuccess(response.body() as BaseResponse)
            }

            override fun onFailure(call: Call<BaseResponse>, t: Throwable) {
                view.onGalleryImgFailure(t.message ?: "통신 오류")
            }
        })
    }
}

interface GalleryImgRetrofitInterface {

    @POST("/mypage/profile/image")
    fun postGalleryImg(@Header("token")token:String, @Part uploadImage:MultipartBody.Part?): Call<BaseResponse>
}

interface GalleryImgFragmentView {

    fun onGalleryImgPostSuccess(response: BaseResponse)

    fun onGalleryImgFailure(message: String)
}