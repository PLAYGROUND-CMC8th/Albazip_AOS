package com.example.albazip.src.mypage.manager.workerlist.custom

import android.R.attr
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.albazip.config.BaseResponse
import com.example.albazip.databinding.DialogFragmentCardPositionDeleteBinding
import com.example.albazip.src.mypage.manager.MMyPageFragment
import com.example.albazip.src.mypage.manager.workerlist.cardevent.ui.CardNoWorkerFragment
import com.example.albazip.src.mypage.manager.workerlist.network.DelPositionFragmentView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class DelPositionBottomSheetDialog(positionId:Int): BottomSheetDialogFragment(),DelPositionFragmentView {

    private var getPositionId = positionId
    private lateinit var binding: DialogFragmentCardPositionDeleteBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogFragmentCardPositionDeleteBinding.inflate(inflater, container, false)

        Log.d("positioId",getPositionId.toString())

        // 취소 버튼
        binding.btnCancel.setOnClickListener {
            dismiss()
        }

        // 포지션 삭제
        binding.btnDelete.setOnClickListener {

            dismiss()
          //  val transaction = parentFragmentManager.beginTransaction()
          //  transaction.hide(this)
          //  transaction.commit()
//            val nextIntent = Intent(requireContext(),ManagerMainActivity::class.java)
//            startActivity(nextIntent)
//            activity?.finishAffinity()
           // DelPositionService(this).tryDelWorkerPosition(getPositionId)
        }

        return binding.root
    }

    override fun onPositionDelSuccess(response: BaseResponse) {


        // 프래그먼트 종료
        activity?.finish()
        dismiss()

    }

    override fun onPositionDelFailure(message: String) {
    }
}