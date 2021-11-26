package com.example.albazip.src.mypage.manager.workerlist.cardevent.ui

import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.bumptech.glide.Glide
import com.example.albazip.R
import com.example.albazip.config.BaseFragment
import com.example.albazip.databinding.FragmentCardInfoBinding
import com.example.albazip.src.mypage.manager.workerlist.cardevent.data.*
import com.example.albazip.src.mypage.manager.workerlist.cardevent.network.EmptyWorkerService
import com.example.albazip.src.mypage.manager.workerlist.cardevent.network.ExistWorkerFragmentView
import com.example.albazip.src.mypage.manager.workerlist.cardevent.network.ExistWorkerService
import com.example.albazip.src.mypage.manager.workerlist.outworker.custom.ResponseWorkerOutBottomSheetDialog
import com.example.albazip.src.mypage.worker.init.data.PositionInfo
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

// 카드에 근무자 존재
class CardExistWorkerFragment(val positionId:Int,val outStatus:Int):
    BaseFragment<FragmentCardInfoBinding>(FragmentCardInfoBinding::bind, R.layout.fragment_card_info),ExistWorkerFragmentView {

    // 퇴사 요청 여부 체크
    private val getOutStatus = outStatus

    private val tabTextList = arrayListOf("근무자 정보", "포지션 정보", "업무 리스트")
    private val getPositionId = positionId // 근무자 고유 id

    // 근무자 존재 데이터
    private lateinit var workerInfo: ExistWorkerInfo

    // 공통
    private lateinit var positionInfo: PositionInfo // 포지션 정보
    private lateinit var positionTaskList: ArrayList<PositionTaskList>  // ????

    // dialog에 전달할 이름
    var worker_first_name = ""

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // sticky tab layout
        binding.stickyNestedScrollview.run {
            header = binding.clTab
            stickListener = { _ ->
                Log.d("LOGGER_TAG", "stickListener")
            }
            freeListener = { _ ->
                Log.d("LOGGER_TAG", "freeListener")
            }
        }

        // 탭 레이아웃 커스튬
        tabTextStyle()

        // 뒤로가기
        binding.tbrIbtnBackBtn.setOnClickListener {
            val transaction = parentFragmentManager.beginTransaction()
            transaction.detach(this)
            transaction.commit()
        }

        // 서버통신 시작
        ExistWorkerService(this).tryGetExistCard(getPositionId)
        showLoadingDialog(requireContext())


    }

    private fun init() {
        binding.viewpager.adapter = CustomFragmentStateAdapter(requireActivity())
        TabLayoutMediator(binding.tabLayout, binding.viewpager) { tab, position ->
            tab.text = tabTextList[position]
        }.attach()
    }

    /* 2개의 프래그먼트를 달아줄 어댑터 */
    inner class CustomFragmentStateAdapter(fragmentActivity: FragmentActivity) :
        FragmentStateAdapter(fragmentActivity) {
        override fun getItemCount(): Int {
            return 3
        }

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> CardWorkerInfoChildFragment(workerInfo,getPositionId,worker_first_name) // 근무자 존재
                1 -> CardPositionChildFragment(positionInfo,1,positionId) // 포지션 정보
                2 -> CardToDoChildFragment(positionTaskList,positionId)// 업무 리스트
                else -> CardWorkerInfoChildFragment(workerInfo,getPositionId,worker_first_name)
            }
        }
    }

    private fun tabTextStyle() {
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.let {
                    setStyleForTab(it, Typeface.BOLD)
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                tab?.let {
                    setStyleForTab(it, Typeface.NORMAL)
                }
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })
    }

    fun setStyleForTab(tab: TabLayout.Tab, style: Int) {
        tab.view.children.find { it is TextView }?.let { tv ->
            (tv as TextView).post {
                tv.setTypeface(null, style)
            }
        }
    }

    override fun onGetSuccess(response: ExistWorkerResponse) {
        dismissLoadingDialog()
        if(response.code == 200){

            // 프로필 설정
            if(response.data.positionProfile.imagePath != "null") {
                Glide.with(requireContext()).load(response.data.positionProfile.imagePath).circleCrop().into(binding.ivProfile)
            }else{ // null 이면 기본이미지 보여주기
                Glide.with(requireContext()).load(R.drawable.img_profile_w_58_px_2).circleCrop().into(binding.ivProfile)
            }

            binding.tvPosition.text = response.data.positionProfile.title // 포지션

            binding.tvFirstName.text = response.data.positionProfile.firstName // 이름
            worker_first_name = response.data.positionProfile.firstName

            binding.tvRank.text = response.data.positionProfile.rank // 직위

            positionInfo = response.data.positionInfo

            workerInfo = response.data.workerInfo

            positionTaskList = response.data.positionTaskList
            //positionTaskList = response.data.positionTaskList.
        }
        init()

        // 퇴사요청한 근무자를 클릭하고 들어왔을 때
        if(getOutStatus == 2){
            ResponseWorkerOutBottomSheetDialog(getPositionId,worker_first_name).show(childFragmentManager,"response_out")
        }
    }

    override fun onGetFailure(message: String) {
        dismissLoadingDialog()
        showCustomToast(message)
    }
}