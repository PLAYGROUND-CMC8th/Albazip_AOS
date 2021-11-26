package com.example.albazip.src.mypage.manager.workerlist.custom

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.albazip.databinding.DialogFragmentCardPositionDeleteBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class DelPositionBottomSheetDialog(): BottomSheetDialogFragment(){

    private lateinit var binding: DialogFragmentCardPositionDeleteBinding
    lateinit var bottomSheetClickListener:BottomSheetClickListener

    override fun onAttach(context: Context) {
        super.onAttach(context)

        bottomSheetClickListener = requireParentFragment() as BottomSheetClickListener
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogFragmentCardPositionDeleteBinding.inflate(inflater, container, false)

        // 취소 버튼
        binding.btnCancel.setOnClickListener {
            dismiss()
        }

        // 포지션 삭제
        binding.btnDelete.setOnClickListener {
            bottomSheetClickListener.onItemSelected(true)
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


    interface BottomSheetClickListener{
        fun onItemSelected(isDeleteClicked:Boolean)
    }

}