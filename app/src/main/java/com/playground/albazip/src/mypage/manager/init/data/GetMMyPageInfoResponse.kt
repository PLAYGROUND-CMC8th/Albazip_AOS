package com.playground.albazip.src.mypage.manager.init.data

import com.playground.albazip.config.BaseResponse
import com.playground.albazip.src.community.manager.network.CommuNoticeInfo
import com.google.gson.annotations.SerializedName


data class GetMMyPageInfoResponse (
    @SerializedName("data") val data : MMyPageData
):BaseResponse()

////////////////////////////// 데이터 ////////////////////////////////////////
data class MMyPageData(
    @SerializedName("profileInfo") val profileInfo : ProfileInfo,
    @SerializedName("workerList") val workerList : ArrayList<WorkerList>?,
    @SerializedName("boardInfo") val boardInfo : BoardInfo
)

///////////////////////// 관리자 기본 정보 ////////////////////////////////
data class ProfileInfo(
    @SerializedName("shopName") val shopName : String,
    @SerializedName("jobTitle") val jobTitle : String,
    @SerializedName("lastName") val lastName : String,
    @SerializedName("firstName") val firstName : String,
    @SerializedName("imagePath") val imagePath : String,
)

//////////////////////// 근무자 리스트 //////////////////////////////////////////
data class WorkerList(
    @SerializedName("positionId") val positionId : Int,
    @SerializedName("status") val status : Int,
    @SerializedName("rank") val rank : String,
    @SerializedName("image_path") val image_path : String?,
    @SerializedName("title") val title : String,
    @SerializedName("first_name") val first_name : String,
)


/////////////////////////// 공지사항 및 게시판 ///////////////////////////////////
data class BoardInfo(
    @SerializedName("noticeInfo") val noticeInfo : ArrayList<CommuNoticeInfo>,
    @SerializedName("postInfo") val postInfo : ArrayList<PostInfo>,
)

data class NoticeInfo(
    @SerializedName("id") val id : Int,
    @SerializedName("pin") val pin : Int,
    @SerializedName("title") val title : String,
    @SerializedName("registerDate") val registerDate : String,
)

data class PostInfo(
    @SerializedName("id") val id : Int,
    @SerializedName("writerJob") val writerJob : String,
    @SerializedName("writerName") val writerName : String,
    @SerializedName("title") val title : String,
    @SerializedName("content") val content : String,
    @SerializedName("commentCount") val commentCount : Int,
    @SerializedName("registerDate") val registerDate : String,
)