package com.example.albazip.src.register.worker.data

import com.google.gson.annotations.SerializedName

data class PostSignInWorkerRequest(
    @SerializedName("code") val code: String,
)