package com.playground.albazip.src.mypage.manager.board.data.remote

import com.playground.albazip.config.BaseResponse
import com.playground.albazip.src.mypage.manager.init.data.BoardInfo
import com.google.gson.annotations.SerializedName

data class GetBoardResponse(
    @SerializedName("data") val data: BoardInfo
) : BaseResponse()