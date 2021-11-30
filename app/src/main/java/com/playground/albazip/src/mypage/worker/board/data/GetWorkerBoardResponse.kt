package com.playground.albazip.src.mypage.worker.board.data

import com.playground.albazip.config.BaseResponse
import com.playground.albazip.src.mypage.worker.init.data.WPostInfo
import com.google.gson.annotations.SerializedName

data class GetWorkerBoardResponse (
    @SerializedName("data") val boardData: BoardData,

    ):BaseResponse()

data class BoardData(
    @SerializedName("postInfo") val postInfo: ArrayList<WPostInfo>,
)
