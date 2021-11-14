package com.example.albazip.src.mypage.manager.workerlist.cardevent.ui

import android.os.Bundle
import android.view.View
import com.example.albazip.R
import com.example.albazip.config.BaseFragment
import com.example.albazip.databinding.ChildFragmentCardToDoListBinding

class CardToDoChildFragment(positionTaskList:ArrayList<String>):BaseFragment<ChildFragmentCardToDoListBinding>(ChildFragmentCardToDoListBinding::bind,
    R.layout.child_fragment_card_to_do_list) {

    private val getPositionTaskList = positionTaskList

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }
}