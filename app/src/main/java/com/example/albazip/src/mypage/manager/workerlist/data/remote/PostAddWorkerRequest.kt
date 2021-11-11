package com.example.albazip.src.mypage.manager.workerlist.data.remote

import com.google.gson.annotations.SerializedName

data class PostAddWorkerRequest (
    @SerializedName("rank") val rank : String,
    @SerializedName("title") val title : String,
    @SerializedName("startTime") val startTime : String,
    @SerializedName("endTime") val endTime : String,
    @SerializedName("workDays") val workDays : ArrayList<String>,
    @SerializedName("breakTime") val breakTime : String,
    @SerializedName("salary") val salary : String,
    @SerializedName("salaryType") val salaryType : String,
    @SerializedName("taskLists") val taskLists : ArrayList<TaskLists>?
)

data class TaskLists(
    @SerializedName("title") val title : String,
    @SerializedName("content") val content : String
)