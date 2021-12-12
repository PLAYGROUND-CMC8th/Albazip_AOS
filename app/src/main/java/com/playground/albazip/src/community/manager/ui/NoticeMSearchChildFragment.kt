package com.playground.albazip.src.community.manager.ui

import android.os.Bundle
import android.view.View
import com.playground.albazip.R
import com.playground.albazip.config.BaseFragment
import com.playground.albazip.databinding.ChildFragmentSearchResultBinding
import com.playground.albazip.src.community.common.network.SearchResponse
import com.playground.albazip.src.community.manager.adapter.NoticeListAdapter
import com.playground.albazip.src.mypage.manager.board.data.local.NoticeData

class NoticeMSearchChildFragment(searchResponse:SearchResponse): BaseFragment<ChildFragmentSearchResultBinding>(
    ChildFragmentSearchResultBinding::bind,
    R.layout.child_fragment_search_result) {

    private var noticeArray = ArrayList<NoticeData>()
    private lateinit var noticeAdapter: NoticeListAdapter
    private var searchResponseData = searchResponse


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        noticeArray.clear()

        for (i in 0 until searchResponseData.data.size){
            noticeArray.add(NoticeData(searchResponseData.data[i].id ,searchResponseData.data[i].title,searchResponseData.data[i].registerDate.substring(0, 10).replace("-", ".") + ".",searchResponseData.data[i].pin))
        }

        noticeAdapter = NoticeListAdapter(requireContext(),noticeArray)

        binding.rvCommunityMainNotice.adapter = noticeAdapter
    }

}