package com.playground.albazip.src.community.worker.ui

import android.os.Bundle
import android.view.View
import com.playground.albazip.R
import com.playground.albazip.config.BaseFragment
import com.playground.albazip.databinding.ChildFragmentSearchResultBinding
import com.playground.albazip.src.community.common.network.SearchResponse
import com.playground.albazip.src.community.worker.adapter.NoticeWListAdapter
import com.playground.albazip.src.community.worker.data.NoticeWData

class NoticeWSearchChildFragment(searchResponse: SearchResponse) :
    BaseFragment<ChildFragmentSearchResultBinding>(
        ChildFragmentSearchResultBinding::bind,
        R.layout.child_fragment_search_result
    ) {

    private var noticeArray = ArrayList<NoticeWData>()
    private lateinit var noticeAdapter: NoticeWListAdapter
    private var searchResponseData = searchResponse

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        noticeArray.clear()

        for (i in 0 until searchResponseData.data.size) {
            noticeArray.add(
                NoticeWData(
                    searchResponseData.data[i].id,
                    searchResponseData.data[i].title,
                    searchResponseData.data[i].registerDate.substring(0, 10)
                        .replace("-", ".") + ".",
                    searchResponseData.data[i].pin,
                    searchResponseData.data[i].confirm
                )
            )
        }

        noticeAdapter = NoticeWListAdapter(requireContext(), noticeArray)
        binding.rvCommunityMainNotice.adapter = noticeAdapter
    }
}