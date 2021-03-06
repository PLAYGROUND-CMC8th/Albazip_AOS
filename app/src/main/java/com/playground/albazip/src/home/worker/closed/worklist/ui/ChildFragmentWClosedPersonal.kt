package com.playground.albazip.src.home.worker.closed.worklist.ui

import android.os.Bundle
import android.view.View
import com.playground.albazip.R
import com.playground.albazip.config.BaseFragment
import com.playground.albazip.databinding.ChildFragmentWTodoListBinding
import com.playground.albazip.databinding.DialogTodoAllDoneBinding
import com.playground.albazip.src.home.worker.closed.worklist.adapter.FHDoneAdapter
import com.playground.albazip.src.home.worker.closed.worklist.adapter.FHWUnDoneAdapter
import com.playground.albazip.src.home.worker.opened.worklist.data.HDoneWorkListData
import com.playground.albazip.src.home.worker.opened.worklist.data.HUnDoneWorkListData
import com.playground.albazip.src.home.worker.opened.worklist.network.GetWPerTaskFragmentView
import com.playground.albazip.src.home.worker.opened.worklist.network.GetWPerTaskResponse
import com.playground.albazip.src.home.worker.opened.worklist.network.GetWPerTaskService
import com.playground.albazip.src.home.worker.opened.worklist.network.WTodayTaskResult

class ChildFragmentWClosedPersonal(data: WTodayTaskResult?): BaseFragment<ChildFragmentWTodoListBinding>(
    ChildFragmentWTodoListBinding::bind,
    R.layout.child_fragment_w_todo_list), GetWPerTaskFragmentView {

    private var ResultData = data

    // 미완료 리스트
    private var unDoneList = ArrayList<HUnDoneWorkListData>()
    private lateinit var unDoneAdapter: FHWUnDoneAdapter

    // 완료 리스트
    private var doneList = ArrayList<HDoneWorkListData>()
    private lateinit var doneAdpater: FHDoneAdapter

    // 다이얼로그 바인딩
    private lateinit var dialogBinding: DialogTodoAllDoneBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 완료 인원 뷰 가리기
        binding.rlDonePersonCnt.visibility = View.INVISIBLE

        dialogBinding = DialogTodoAllDoneBinding.inflate(layoutInflater)

        if(ResultData?.perTask?.nonComPerTask?.size != null){
            for(i in 0 until ResultData!!.perTask.nonComPerTask.size){
                var content = ResultData!!.perTask.nonComPerTask[i].taskContent
                if (content == "null" || content.isEmpty()){
                    content = "내용없음"
                }

                var writerAndDay = ResultData!!.perTask.nonComPerTask[i].writerTitle + " " + ResultData!!.perTask.nonComPerTask[i].writerName + " · " + ResultData!!.perTask.nonComPerTask[i].registerDate.substring(0, 10).replace("-", ".") + "."
                //var writerAndDay = ResultData!!.perTask.nonComPerTask[i].writerTitle + " · " + ResultData!!.perTask.nonComPerTask[i].writerName + ResultData!!.perTask.nonComPerTask[i].registerDate.substring(0, 10).replace("-", ".") + "."

                unDoneList.add(HUnDoneWorkListData(1,ResultData!!.perTask.nonComPerTask[i].taskId,0,ResultData!!.perTask.nonComPerTask[i].takTitle,content,writerAndDay))
            }
        }
        unDoneAdapter = FHWUnDoneAdapter(unDoneList,requireContext(),dialogBinding.root)
        binding.rvUndone.adapter = unDoneAdapter



        if(ResultData?.perTask?.compPerTask?.size !=null){
            for(i in 0 until ResultData!!.perTask.compPerTask.size){
                doneList.add(HDoneWorkListData(ResultData!!.perTask.compPerTask[i].taskId,1,ResultData!!.perTask.compPerTask[i].takTitle,"완료 "+ResultData!!.perTask.compPerTask[i].completeTime.substring(11, 16)))
            }
        }
        doneAdpater = FHDoneAdapter(parentFragmentManager,requireContext(),doneList)
        binding.rvDone.adapter = doneAdpater

        // ui 체크
        checkingUI()
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

    // 화면 갱신
    override fun onResume() {
        super.onResume()
        GetWPerTaskService(this).tryGetPerTask()
    }

    // 개인업무 조회 성공
    override fun onGetWPerTaskSuccess(response: GetWPerTaskResponse) {
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

                var writerAndDay = response.data.nonComPerTask[i].writerTitle + " " + response.data.nonComPerTask[i].writerName + " · " + response.data.nonComPerTask[i].registerDate.substring(0, 10).replace("-", ".") + "."
                // var writerAndDay = response.data.nonComPerTask[i].writerTitle + " · " + response.data.nonComPerTask[i].writerName + response.data.nonComPerTask[i].registerDate.substring(0, 10).replace("-", ".") + "."

                unDoneList.add(HUnDoneWorkListData(1,response.data.nonComPerTask[i].taskId,0,response.data.nonComPerTask[i].takTitle,content,writerAndDay))
            }
        }else{
            unDoneList.clear()
            binding.rvUndone.recycledViewPool.clear()
            unDoneAdapter.notifyDataSetChanged()
        }
        unDoneAdapter = FHWUnDoneAdapter(unDoneList,requireContext(),dialogBinding.root)
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
                doneList.add(HDoneWorkListData(response.data.compPerTask[i].taskId,1,response.data.compPerTask[i].takTitle,"완료 "+response.data.compPerTask[i].completeTime.substring(11, 16)))
            }
        }else{
            doneList.clear()
            binding.rvDone.recycledViewPool.clear()
            doneAdpater.notifyDataSetChanged()
        }
        doneAdpater = FHDoneAdapter(parentFragmentManager,requireContext(),doneList)
        binding.rvDone.adapter = doneAdpater

        checkingUI()
    }

    override fun onGetWPerTaskFailure(message: String) {
    }

}