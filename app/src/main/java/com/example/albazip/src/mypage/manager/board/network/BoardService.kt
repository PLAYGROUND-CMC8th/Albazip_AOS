package com.example.albazip.src.mypage.manager.board.network

import com.example.albazip.config.ApplicationClass
import com.example.albazip.src.mypage.manager.board.data.remote.GetBoardResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

class BoardService(val view: BoardFragmentView) {

    fun tryGetBoard(){
        val boardRetrofitInterface = ApplicationClass.sRetrofit.create(BoardRetrofitInterface::class.java)
        val token = ApplicationClass.prefs.getString("X-ACCESS-TOKEN","0")
        boardRetrofitInterface.getBoard(token).enqueue(object :
            Callback<GetBoardResponse> {
            override fun onResponse(call: Call<GetBoardResponse>, response: Response<GetBoardResponse>) {
                view.onBoardGetSuccess(response.body() as GetBoardResponse)
            }

            override fun onFailure(call: Call<GetBoardResponse>, t: Throwable) {
                view.onBoardGetFailure(t.message ?: "통신 오류")
            }
        })
    }
}

interface BoardRetrofitInterface {
    @GET("/mypage/boards/manager")
    fun getBoard(@Header("token")token:String): Call<GetBoardResponse>
}

interface BoardFragmentView {

    fun onBoardGetSuccess(response: GetBoardResponse)

    fun onBoardGetFailure(message: String)
}