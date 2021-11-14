package com.example.albazip.src.mypage.manager.board.data.remote

import com.example.albazip.config.BaseResponse
import com.example.albazip.src.mypage.manager.init.data.BoardInfo
import com.google.gson.annotations.SerializedName

data class GetBoardResponse(
    @SerializedName("data") val data: BoardInfo
) : BaseResponse()