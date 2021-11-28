package com.example.albazip.src.home.worker.opened.worklist.ui

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import com.example.albazip.R
import com.example.albazip.config.BaseFragment
import com.example.albazip.databinding.ChildFragmentWTodoListBinding
import com.example.albazip.databinding.DialogTodoAllDoneBinding
import com.example.albazip.src.home.common.adapter.DoneWorkerCntAdapter
import com.example.albazip.src.home.common.data.DoneWorkerCntData
import com.example.albazip.src.home.worker.opened.worklist.adapter.HDoneAdapter
import com.example.albazip.src.home.worker.opened.worklist.adapter.HTogetherDoneAdapter
import com.example.albazip.src.home.worker.opened.worklist.adapter.HUnDoneAdapter
import com.example.albazip.src.home.worker.opened.worklist.data.HDoneWorkListData
import com.example.albazip.src.home.worker.opened.worklist.data.HUnDoneWorkListData
import com.example.albazip.src.home.worker.opened.worklist.network.GetWPerTaskFragmentView
import com.example.albazip.src.home.worker.opened.worklist.network.GetWPerTaskResponse
import com.example.albazip.src.home.worker.opened.worklist.network.GetWPerTaskService
import com.example.albazip.src.home.worker.opened.worklist.network.WTodayTaskResult

class ChildFragmentWPersonal(data: WTodayTaskResult?): BaseFragment<ChildFragmentWTodoListBinding>(
    ChildFragmentWTodoListBinding::bind,
    R.layout.child_fragment_w_todo_list),GetWPerTaskFragmentView {

    private var ResultData = data

    // 미완료 리스트
    private var unDoneList = ArrayList<HUnDoneWorkListData>()
    private lateinit var unDoneAdapter: HUnDoneAdapter

    // 완료 리스트
    private var doneList = ArrayList<HDoneWorkListData>()
    private lateinit var doneAdpater: HDoneAdapter

    // 다이얼로그 바인딩
    private lateinit var dialogBinding:DialogTodoAllDoneBinding

    // 인원수 체크 버튼 클릭 여부
    var clickedCnt = false
    private var doneWorkerCntList = ArrayList<DoneWorkerCntData>()
    private lateinit var doneWorkerCntAdapter:DoneWorkerCntAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dialogBinding = DialogTodoAllDoneBinding.inflate(layoutInflater)

        if(ResultData?.perTask?.nonComPerTask?.size != null){
            for(i in 0 until ResultData!!.perTask.nonComPerTask.size){
                var content = ResultData!!.perTask.nonComPerTask[i].taskContent
                if (content == "null" || content.isEmpty()){
                    content = "내용없음"
                }

                var writerAndDay = ResultData!!.perTask.nonComPerTask[i].writerTitle + " · " + ResultData!!.perTask.nonComPerTask[i].writerName + ResultData!!.perTask.nonComPerTask[i].registerDate.substring(0,9)

                unDoneList.add(HUnDoneWorkListData(1,ResultData!!.perTask.nonComPerTask[i].taskId,0,ResultData!!.perTask.nonComPerTask[i].takTitle,content,writerAndDay))
            }
        }
        unDoneAdapter = HUnDoneAdapter(unDoneList,requireContext(),dialogBinding.root)
        binding.rvUndone.adapter = unDoneAdapter



        if(ResultData?.perTask?.compPerTask?.size !=null){
            for(i in 0 until ResultData!!.perTask.compPerTask.size){
                doneList.add(HDoneWorkListData(0,ResultData!!.perTask.compPerTask[i].takTitle,"완료 "+ResultData!!.perTask.compPerTask[i].completeTime))
            }
        }
        doneAdpater = HDoneAdapter(requireContext(),doneList)
        binding.rvDone.adapter = doneAdpater

        // ui 체크
        checkingUI()



        doneWorkerCntList.add(DoneWorkerCntData("","평일마감","지연",1))
        doneWorkerCntList.add(DoneWorkerCntData("","평일마감","주연",2))
        doneWorkerCntList.add(DoneWorkerCntData("","평일마감","수빈",1))
        doneWorkerCntList.add(DoneWorkerCntData("","평일마감","희영",3))

        doneWorkerCntAdapter = DoneWorkerCntAdapter(doneWorkerCntList)
        binding.rvDoneWorkerList.adapter = doneWorkerCntAdapter

        // 인원수 체크 레이아웃
        binding.rlDonePersonCnt.setOnClickListener {

            if(clickedCnt == false) {
                // pop up 활성화 상태 -> 터치 대기
                clickedCnt = true
                // bg 도 변경하기
                binding.rlDonePersonCnt.background = ContextCompat.getDrawable(
                        requireContext(),
                R.drawable.rectangle_gray_radius_7
                )
                binding.frameLayoutCntDoneWorker.visibility = View.VISIBLE
            }else{
                clickedCnt = false
                binding.rlDonePersonCnt.background = ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.rectangle_light_gray_radius_7
                )
                binding.frameLayoutCntDoneWorker.visibility = View.GONE
            }
        }
    }

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


        if(unDoneList.size == 0 && doneList.size !=0){ // 모든 업무 완료
            binding.rlNoUndoneWork.visibility = View.VISIBLE
        }else{
            binding.rlNoUndoneWork.visibility = View.GONE
        }

        if(unDoneList.size !=0 && doneList.size ==0){ // 완료된 업무가 없어요
            binding.rlNoDoneWork.visibility = View.VISIBLE
        }else{
            binding.rlNoUndoneWork.visibility = View.GONE
        }
    }

    // 화면 갱신
    override fun onResume() {
        super.onResume()
        GetWPerTaskService(this).tryGetPerTask()
        showLoadingDialog(requireContext())
    }

    // 개인업무 조회 성공
    override fun onGetWPerTaskSuccess(response: GetWPerTaskResponse) {
        dismissLoadingDialog()

        // 미완료 리스트 조회
        // 기존 데이터 비우기
        if(unDoneList.size != 0) {
            unDoneList.clear()
            binding.rvUndone.recycledViewPool.clear()
            unDoneAdapter.notifyDataSetChanged()
        }

        if(response.data.nonComPerTask.size != 0){
            for(i in 0 until response.data.nonComPerTask.size){

                var content = response.data.nonComPerTask[i].taskContent
                if (content == "null" || content.isEmpty()){
                    content = "내용없음"
                }

                var writerAndDay = response.data.nonComPerTask[i].writerTitle + " · " + response.data.nonComPerTask[i].writerName + response.data.nonComPerTask[i].registerDate.substring(0,9)

                unDoneList.add(HUnDoneWorkListData(1,response.data.nonComPerTask[i].taskId,0,response.data.nonComPerTask[i].takTitle,content,writerAndDay))
            }
        }else{
            unDoneList.clear()
            binding.rvUndone.recycledViewPool.clear()
            unDoneAdapter.notifyDataSetChanged()
        }
        unDoneAdapter = HUnDoneAdapter(unDoneList,requireContext(),dialogBinding.root)
        binding.rvUndone.adapter = unDoneAdapter


        // 완료 리스트 조회
        // 기존 리스트 지우기
        if(doneList.size != 0) {
            doneList.clear()
            binding.rvDone.recycledViewPool.clear()
            doneAdpater.notifyDataSetChanged()
        }
        if(response.data.compPerTask.size != 0){
            for(i in 0 until response.data.compPerTask.size){
                doneList.add(HDoneWorkListData(0,response.data.compPerTask[i].takTitle,"완료 "+response.data.compPerTask[i].completeTime.substring(11, 16)))
            }
        }else{
            doneList.clear()
            binding.rvDone.recycledViewPool.clear()
            doneAdpater.notifyDataSetChanged()
        }
        doneAdpater = HDoneAdapter(requireContext(),doneList)
        binding.rvDone.adapter = doneAdpater

        checkingUI()
    }

    override fun onGetWPerTaskFailure(message: String) {
        dismissLoadingDialog()
    }


}