package com.playground.albazip.src.home.worker.opened.worklist.ui

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.PopupWindow
import com.playground.albazip.R
import com.playground.albazip.config.BaseFragment
import com.playground.albazip.databinding.BgCntReadPopupBinding
import com.playground.albazip.databinding.ChildFragmentWTodoListBinding
import com.playground.albazip.databinding.DialogTodoAllDoneBinding
import com.playground.albazip.src.home.common.adapter.DoneWorkerCntAdapter
import com.playground.albazip.src.home.common.data.DoneWorkerCntData
import com.playground.albazip.src.home.common.data.HomeCoWorkResponse
import com.playground.albazip.src.home.common.network.GetHomeCoWorkFragmentView
import com.playground.albazip.src.home.common.network.GetHomeCoWorkService
import com.playground.albazip.src.home.worker.opened.worklist.adapter.HWDoneAdapter
import com.playground.albazip.src.home.worker.opened.worklist.adapter.HWUnDoneAdapter
import com.playground.albazip.src.home.worker.opened.worklist.data.HDoneWorkListData
import com.playground.albazip.src.home.worker.opened.worklist.data.HUnDoneWorkListData
import com.playground.albazip.src.home.worker.opened.worklist.network.WTodayTaskResult

class ChildFragmentWTogether(data: WTodayTaskResult?) : BaseFragment<ChildFragmentWTodoListBinding>(
    ChildFragmentWTodoListBinding::bind,
    R.layout.child_fragment_w_todo_list), GetHomeCoWorkFragmentView {

    private var ResultData = data

    // 미완료 리스트
    private var unDoneList = ArrayList<HUnDoneWorkListData>()
    private lateinit var unDoneAdapter:HWUnDoneAdapter

    // 완료 리스트
    private var doneList = ArrayList<HDoneWorkListData>()
    private lateinit var doneAdpater:HWDoneAdapter

    // 팝업 view
    private lateinit var popUpBinding: BgCntReadPopupBinding

    // 업무 완료자 리스트
    private var workerCntList = ArrayList<DoneWorkerCntData>()
    private lateinit var doneWorkerCntAdapter: DoneWorkerCntAdapter

    // 다이얼로그 바인딩
    private lateinit var dialogBinding: DialogTodoAllDoneBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dialogBinding = DialogTodoAllDoneBinding.inflate(layoutInflater)

        popUpBinding = BgCntReadPopupBinding.inflate(layoutInflater)

        // 미완료 리스트가 없으면 (배열 개수 0)
        //unDoneList.add(HUnDoneWorkListData(0,"제목1","내용1","작성 날짜 및 작성자"))
        if(ResultData?.coTask?.nonComCoTask?.size != null){
            for(i in 0 until ResultData?.coTask?.nonComCoTask!!.size){

                var content = ResultData!!.coTask.nonComCoTask[i].taskContent
                if (content == "null" || content.isEmpty()){
                    content = "내용없음"
                }

                var writerAndDay = ResultData!!.coTask.nonComCoTask[i].writerTitle + " " + ResultData!!.coTask.nonComCoTask[i].writerName + " · " + ResultData!!.coTask.nonComCoTask[i].registerDate.substring(0, 10).replace("-", ".") + "."
                // var writerAndDay = ResultData!!.coTask.nonComCoTask[i].writerTitle + " · " + ResultData!!.coTask.nonComCoTask[i].writerName + ResultData!!.coTask.nonComCoTask[i].registerDate.substring(0, 10).replace("-", ".") + "."

                unDoneList.add(HUnDoneWorkListData(1,ResultData!!.coTask.nonComCoTask[i].taskId,0,ResultData!!.coTask.nonComCoTask[i].takTitle,content,writerAndDay))
            }
        }
        unDoneAdapter = HWUnDoneAdapter(unDoneList,requireContext(),dialogBinding.root)
        binding.rvUndone.adapter = unDoneAdapter

        if(ResultData?.coTask?.comCoTask?.size != null){
            for(i in 0 until ResultData?.coTask?.comCoTask!!.size){
                doneList.add(HDoneWorkListData(ResultData!!.coTask.comCoTask[i].taskId,1,ResultData!!.coTask.comCoTask[i].takTitle,"완료 "+ResultData!!.coTask.comCoTask[i].completeTime.substring(11, 16)))
            }
        }
        // 완료 리스트가 없으면 (배열 개수 0)
        //doneList.add(HDoneWorkListData(0,"제목1","시간"))
        doneAdpater = HWDoneAdapter(parentFragmentManager,requireContext(),doneList)
        binding.rvDone.adapter = doneAdpater

        // 완료한 사람 목록 adpater
        for(i in 0 until ResultData?.coTask?.comWorker?.comWorker!!.size){
            var workerInfo = ResultData!!.coTask.comWorker.comWorker[i].worker!!.split(" ")
            workerCntList.add(DoneWorkerCntData(ResultData!!.coTask.comWorker.comWorker[i].image!!,workerInfo[0],workerInfo[1],ResultData!!.coTask.comWorker.comWorker[i].count))
        }

        doneWorkerCntAdapter = DoneWorkerCntAdapter(workerCntList,requireContext())
        popUpBinding.rvDoneWorkerList.adapter = doneWorkerCntAdapter

        binding.tvDonePersonCnt.text = ResultData?.coTask?.comWorker?.comWorker!!.size.toString()

        // 팝업 띄우기 -> 사람 목록을 눌렀을 때
        binding.rlDonePersonCnt.setOnClickListener {

            // dp to px 단위변경
            val density = resources.displayMetrics.density
            val w_value = (140 * density).toInt()
            val h_value = (170 * density).toInt()
            val moved_w_value =  (100 * density).toInt()
            val moved_h_value =  (8 * density).toInt()

            val moved_h_value_3=  ((130+70) * density).toInt()

            // val width = LinearLayout.LayoutParams.WRAP_CONTENT
            val height = LinearLayout.LayoutParams.WRAP_CONTENT
            var focusable = true

            if(workerCntList.size <= 3) {
                val popupWindow = PopupWindow(popUpBinding.root, w_value, height, focusable)
                popupWindow.contentView = popUpBinding.root
                popupWindow.elevation = 5F
                popupWindow.showAsDropDown(binding.rlDonePersonCnt,-(moved_w_value),moved_h_value)
            }else{ // item 이 4개 이상일 때
                val popupWindow = PopupWindow(popUpBinding.root, w_value, h_value, focusable)
                popupWindow.contentView = popUpBinding.root
                popupWindow.elevation = 5F
                popupWindow.showAsDropDown(binding.rlDonePersonCnt,-(moved_w_value),moved_h_value_3)
            }
        }

        checkingUI()
    }

    // 비어있는 화면 및 업무 갯수 체크
    fun checkingUI(){

        // 미완료 cnt
        binding.tvUndoneCnt.text = unDoneList.size.toString()
        // 완료 cnt
        binding.tvDoneCnt.text = doneList.size.toString()

        if (unDoneList.size == 0 && doneList.size == 0){    //없무 없음
            binding.clNoBothWork.visibility = View.VISIBLE
        }else{ // 없무존재
            binding.clNoBothWork.visibility = View.GONE
        }

        if(unDoneList.size ==0){ // 모든 업무 완료
            binding.rlNoUndoneWork.visibility = View.VISIBLE
        }else{
            binding.rlNoUndoneWork.visibility = View.GONE
        }

        if(doneList.size ==0){ // 완료된 업무가 없어요
            binding.rlNoDoneWork.visibility = View.VISIBLE
        }else{
            binding.rlNoDoneWork.visibility = View.GONE
        }
    }


    // 화면 갱신시 재 조회
    override fun onResume() {
        super.onResume()
        GetHomeCoWorkService(this).tryGetHomeCoWork()
        showLoadingDialog(requireContext())
    }

    // 공동 업무 조회(공동 업무 탭 클릭)
    override fun onGetHomeCoWorkSuccess(response: HomeCoWorkResponse) {
        dismissLoadingDialog()

        // 미완료 리스트 조회
        // 기존 데이터 비우기
        if(unDoneList.size != 0) {
            unDoneList.clear()
            binding.rvUndone.recycledViewPool.clear()
            unDoneAdapter.notifyDataSetChanged()
        }

        if(response.data.nonComCoTask.size != 0){
            for(i in 0 until response.data.nonComCoTask.size){

                var content = response.data.nonComCoTask[i].taskContent
                if (content == "null" || content.isEmpty()){
                    content = "내용없음"
                }

                var writerAndDay = response.data.nonComCoTask[i].writerTitle + " " + response.data.nonComCoTask[i].writerName + " · " + response.data.nonComCoTask[i].registerDate.substring(0, 10).replace("-", ".") + "."
                // var writerAndDay = response.data.nonComCoTask[i].writerTitle + " · " + response.data.nonComCoTask[i].writerName + response.data.nonComCoTask[i].registerDate.substring(0, 10).replace("-", ".") + "."

                unDoneList.add(HUnDoneWorkListData(1,response.data.nonComCoTask[i].taskId,0,response.data.nonComCoTask[i].takTitle,content,writerAndDay))
            }
        }else{
            unDoneList.clear()
            binding.rvUndone.recycledViewPool.clear()
            unDoneAdapter.notifyDataSetChanged()
        }
        unDoneAdapter = HWUnDoneAdapter(unDoneList,requireContext(),dialogBinding.root)
        binding.rvUndone.adapter = unDoneAdapter


        // 완료 리스트 조회
        // 기존 리스트 지우기
        if(doneList.size != 0) {
            doneList.clear()
            binding.rvDone.recycledViewPool.clear()
            doneAdpater.notifyDataSetChanged()
        }
        if(response.data.comCoTask.size != 0){
            for(i in 0 until response.data.comCoTask.size){
                doneList.add(HDoneWorkListData(response.data.comCoTask[i].taskId,1,response.data.comCoTask[i].takTitle,"완료 "+response.data.comCoTask[i].completeTime.substring(11, 16)))
            }
        }else{
            doneList.clear()
            binding.rvDone.recycledViewPool.clear()
            doneAdpater.notifyDataSetChanged()
        }
        doneAdpater = HWDoneAdapter(parentFragmentManager,requireContext(),doneList)
        binding.rvDone.adapter = doneAdpater

        checkingUI()
    }

    override fun onGetHomeCoWorkFailure(message: String) {
        dismissLoadingDialog()
    }

}