package com.example.albazip.src.home.common.custom

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.albazip.databinding.DialogFragmentUndoDoneWorkBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class DoneToUnDoneBottomSheetDialog(): BottomSheetDialogFragment() {

    private lateinit var binding: DialogFragmentUndoDoneWorkBinding


    //lateinit var bottomSheetClickListener: DoneCancelBottomSheetDialog.BottomSheetClickListener

    //override fun onAttach(context: Context) {
    //    super.onAttach(context)

    //    bottomSheetClickListener = context as DoneCancelBottomSheetDialog.BottomSheetClickListener
    //}

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogFragmentUndoDoneWorkBinding.inflate(inflater, container, false)


        // 취소 버튼
        binding.btnCancel.setOnClickListener {
            dismiss()
        }

        // 되돌리기 버튼
        binding.btnUndo.setOnClickListener {
            dismiss()
        }

        return binding.root
    }

    /* interface BottomSheetClickListener{
         fun onItemSelected(delView:Boolean)
     }*/
}