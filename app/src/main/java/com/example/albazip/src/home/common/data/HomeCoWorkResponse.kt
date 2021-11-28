package com.example.albazip.src.home.common.data

import com.example.albazip.config.BaseResponse
import com.example.albazip.src.home.manager.worklist.network.ComCoTask
import com.example.albazip.src.home.manager.worklist.network.ComWorker
import com.example.albazip.src.home.manager.worklist.network.NonComCoTask
import com.google.gson.annotations.SerializedName

data class HomeCoWorkResponse(
    @SerializedName("data")val data: HomeCoWorkResult
): BaseResponse()

data class HomeCoWorkResult(
    @SerializedName("nonComCoTask")val nonComCoTask: ArrayList<NonComCoTask>,
    @SerializedName("comWorker")val comWorker: ComWorker,
    @SerializedName("comCoTask")val comCoTask: ArrayList<ComCoTask>,
)