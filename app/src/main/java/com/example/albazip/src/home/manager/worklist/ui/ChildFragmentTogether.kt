package com.example.albazip.src.home.manager.worklist.ui

import android.os.Bundle
import android.view.View
import com.example.albazip.R
import com.example.albazip.config.BaseFragment
import com.example.albazip.databinding.ChildFragmentTogetherBinding
import com.example.albazip.databinding.DialogTodoAllDoneBinding
import com.example.albazip.src.home.common.data.HomeCoWorkResponse
import com.example.albazip.src.home.common.network.GetHomeCoWorkFragmentView
import com.example.albazip.src.home.common.network.GetHomeCoWorkService
import com.example.albazip.src.home.manager.worklist.network.MTodayTaskResult
import com.example.albazip.src.home.worker.opened.worklist.adapter.HTogetherDoneAdapter
import com.example.albazip.src.home.worker.opened.worklist.adapter.HUnDoneAdapter
import com.example.albazip.src.home.worker.opened.worklist.data.HDoneWorkListData
import com.example.albazip.src.home.worker.opened.worklist.data.HUnDoneWorkListData

class ChildFragmentTogether(data: MTodayTaskResult?) : BaseFragment<ChildFragmentTogetherBinding>(
    ChildFragmentTogetherBinding::bind,
    R.layout.child_fragment_together),GetHomeCoWorkFragmentView {

    private var ResultData = data

    // 미완료 리스트
    private var unDoneList = ArrayList<HUnDoneWorkListData>()
    private lateinit var unDoneAdapter: HUnDoneAdapter

    // 완료 리스트
    private var doneList = ArrayList<HDoneWorkListData>()
    private lateinit var doneAdpater: HTogetherDoneAdapter

    // 다이얼로그 바인딩
    private lateinit var dialogBinding: DialogTodoAllDoneBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dialogBinding = DialogTodoAllDoneBinding.inflate(layoutInflater)

        // 미완료 리스트가 없으면 (배열 개수 0)
        //unDoneList.add(HUnDoneWorkListData(0,"제목1","내용1","작성 날짜 및 작성자"))
        if(ResultData?.coTask?.nonComCoTask?.size != null){
        for(i in 0 until ResultData?.coTask?.nonComCoTask!!.size){

            var content = ResultData!!.coTask.nonComCoTask[i].taskContent
            if (content == "null" || content.isEmpty()){
                content = "내용없음"
            }

            var writerAndDay = ResultData!!.coTask.nonComCoTask[i].writerTitle + " · " + ResultData!!.coTask.nonComCoTask[i].writerName + ResultData!!.coTask.nonComCoTask[i].registerDate.substring(0,9)

            unDoneList.add(HUnDoneWorkListData(0,ResultData!!.coTask.nonComCoTask[i].taskId,0,ResultData!!.coTask.nonComCoTask[i].takTitle,content,writerAndDay))
        }
        }
        unDoneAdapter = HUnDoneAdapter(unDoneList,requireContext(),dialogBinding.root)
        binding.rvUndone.adapter = unDoneAdapter


        if(ResultData?.coTask?.comCoTask?.size != null){
            for(i in 0 until ResultData?.coTask?.comCoTask!!.size){
                doneList.add(HDoneWorkListData(ResultData!!.coTask.comCoTask[i].taskId,1,ResultData!!.coTask.comCoTask[i].takTitle,"완료 "+ResultData!!.coTask.comCoTask[i].completeTime.substring(11,16)))
            }
        }
        // 완료 리스트가 없으면 (배열 개수 0)
        //doneList.add(HDoneWorkListData(0,"제목1","시간"))
        doneAdpater = HTogetherDoneAdapter(parentFragmentManager,requireContext(),doneList)
        binding.rvDone.adapter = doneAdpater


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

                var writerAndDay = response.data.nonComCoTask[i].writerTitle + " · " + response.data.nonComCoTask[i].writerName +" "+ response.data.nonComCoTask[i].registerDate.substring(0, 10).replace("-", ".") + "."

                unDoneList.add(HUnDoneWorkListData(0,response.data.nonComCoTask[i].taskId,0,response.data.nonComCoTask[i].takTitle,content,writerAndDay))
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
        if(response.data.comCoTask.size != 0){
            for(i in 0 until response.data.comCoTask.size){
                doneList.add(HDoneWorkListData(response.data.comCoTask[i].taskId,1,response.data.comCoTask[i].takTitle,"완료 "+response.data.comCoTask[i].completeTime.substring(11, 16)))
            }
        }else{
            doneList.clear()
            binding.rvDone.recycledViewPool.clear()
            doneAdpater.notifyDataSetChanged()
        }
        doneAdpater = HTogetherDoneAdapter(parentFragmentManager,requireContext(),doneList)
        binding.rvDone.adapter = doneAdpater

        checkingUI()
    }

    override fun onGetHomeCoWorkFailure(message: String) {
        dismissLoadingDialog()
    }

}