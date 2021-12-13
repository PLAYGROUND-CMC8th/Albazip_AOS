package com.playground.albazip.src.community.common.network

import com.google.gson.annotations.SerializedName
import com.playground.albazip.config.ApplicationClass
import com.playground.albazip.config.BaseResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

class GetNoticeSearchService(val view: GetNoticeSearchFragmentView) {

    fun tryGetNoticeRead(searchWord: String){
        val getNoticeSearchRetrofitInterface = ApplicationClass.sRetrofit.create(
            GetNoticeSearchRetrofitInterface::class.java)
        val token = ApplicationClass.prefs.getString("X-ACCESS-TOKEN","0")
        getNoticeSearchRetrofitInterface.getSearchResult(token,searchWord).enqueue(object :
            Callback<SearchResponse> {
            override fun onResponse(call: Call<SearchResponse>, response: Response<SearchResponse>) {
                view.onGetSearchNoticeSuccess(response.body() as SearchResponse)
            }

            override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                view.onGetSearchNoticeFailure(t.message ?: "통신 오류")
            }
        })
    }
}


interface GetNoticeSearchRetrofitInterface {
    @GET("/board/notice/search/word/{searchWord}")
    fun getSearchResult(@Header("token")token:String, @Path(value = "searchWord", encoded = false) searchWord: String
    ): Call<SearchResponse>
}

interface GetNoticeSearchFragmentView {
    fun onGetSearchNoticeSuccess(response: SearchResponse)
    fun onGetSearchNoticeFailure(message: String)
}

data class SearchResponse(
    @SerializedName("data")val data:ArrayList<SearchResult>,
    @SerializedName("page")val page:Int
    ):BaseResponse()

data class SearchResult(
    @SerializedName("id")val id:Int,
    @SerializedName("pin")val pin:Int,
    @SerializedName("title")val title:String,
    @SerializedName("registerDate")val registerDate:String,
    @SerializedName("confirm")val confirm:Int
)