import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.playground.albazip.databinding.DialogUpdateTimeSetCancelBinding

class RunTimeCancelBottomSheetDialog : BottomSheetDialogFragment() {
    private lateinit var binding: DialogUpdateTimeSetCancelBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogUpdateTimeSetCancelBinding.inflate(inflater, container, false)


        // 취소 버튼
        binding.btnCancel.setOnClickListener {

            dismiss()
        }

        // 나가기 버튼
        binding.btnOut.setOnClickListener {
            activity?.finish()
        }

        return binding.root
    }
}