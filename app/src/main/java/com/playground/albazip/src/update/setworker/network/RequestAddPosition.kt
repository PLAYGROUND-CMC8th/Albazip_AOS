package com.playground.albazip.src.update.setworker.network

import android.os.Parcelable
import com.playground.albazip.src.mypage.manager.workerlist.data.local.TodoData
import java.io.Serializable

data class RequestAddPosition(
    val breakTime: String,
    val rank: String,
    val salary: String,
    val salaryType: String,
    val taskLists: ArrayList<TodoData>?,
    val title: String,
    val workSchedule: List<WorkSchedule>
){
    //data class TaskLists(
    //    val content: String,
    //    val title: String
    //)

    data class WorkSchedule(
        val day: String,
        val startTime: String,
        val endTime: String,
    ):Serializable
}