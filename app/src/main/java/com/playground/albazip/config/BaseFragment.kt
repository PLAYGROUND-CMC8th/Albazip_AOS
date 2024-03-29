package com.playground.albazip.config

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.google.firebase.analytics.FirebaseAnalytics
import com.playground.albazip.util.BeeLoadingDialog
import com.playground.albazip.util.LoadingDialog

// Fragment의 기본을 작성, 뷰 바인딩 활용
abstract class BaseFragment<B : ViewBinding>(
    private val bind: (View) -> B,
    @LayoutRes layoutResId: Int
) : Fragment(layoutResId) {

    var prevFragment: Fragment? = null
    private var _binding: B? = null
    lateinit var mLoadingDialog: LoadingDialog
    lateinit var beeLoadingDialog: BeeLoadingDialog

    protected val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = bind(super.onCreateView(inflater, container, savedInstanceState)!!)
        return binding.root
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    fun showCustomToast(message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
    }

    fun showLoadingDialog(context: Context) {
        mLoadingDialog = LoadingDialog(context)
        mLoadingDialog.show()
    }

    fun dismissLoadingDialog() {
        if (mLoadingDialog.isShowing) {
            mLoadingDialog.dismiss()
        }
    }

    // 꿀벌 로딩 다이얼로그
    fun showBeeLoadingDialog(context: Context){
        beeLoadingDialog = BeeLoadingDialog(context)
        beeLoadingDialog.show()
    }

    // 꿀벌 다이얼로그 없애기
    fun dismissBeeLoadingDialog() {
        if (beeLoadingDialog.isShowing) {
            beeLoadingDialog.dismiss()
        }
    }
}