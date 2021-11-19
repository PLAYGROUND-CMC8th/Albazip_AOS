package com.example.albazip.src.home.common.custom

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import com.example.albazip.databinding.DialogFragmentCancelDoneBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class DoneCancelBottomSheetDialog(checkView:CheckBox,delView:View): BottomSheetDialogFragment() {

    private lateinit var binding: DialogFragmentCancelDoneBinding
    private var checkView = checkView
    private val delView = delView

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
        binding = DialogFragmentCancelDoneBinding.inflate(inflater, container, false)


        // 취소 버튼
        binding.btnCancel.setOnClickListener {
            dismiss()
        }

        // 되돌리기 버튼
        binding.btnReturn.setOnClickListener {
            checkView.isChecked = false
            delView.visibility = View.GONE
            dismiss()
        }

        return binding.root
    }

   /* interface BottomSheetClickListener{
        fun onItemSelected(delView:Boolean)
    }*/
}