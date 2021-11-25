package com.example.albazip.src.mypage.worker.custom

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.example.albazip.R
import com.example.albazip.config.ApplicationClass
import com.example.albazip.config.BaseResponse
import com.example.albazip.databinding.DialogFragmentMSelectProfileBinding
import com.example.albazip.databinding.DialogFragmentWSelectProfileBinding
import com.example.albazip.src.mypage.worker.profile.data.DefaultImgRequest
import com.example.albazip.src.mypage.worker.profile.network.DefaultImgFragmentView
import com.example.albazip.src.mypage.worker.profile.network.DefaultImgService
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.io.ByteArrayOutputStream
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList

class WSelectProfileBottomSheetDialog() : BottomSheetDialogFragment(), View.OnClickListener,DefaultImgFragmentView {

    // 프로필 사진 intent
    private lateinit var getResult: ActivityResultLauncher<Intent>
    private lateinit var binding: DialogFragmentWSelectProfileBinding
    private lateinit var bottomSheetClickListener: BottomSheetClickListener

    // 프로필 체크 Flags 받아오기
    val workerProfileFlags = ApplicationClass.prefs.getInt("workerProfileFlags", 2)
    var runningFlags: Int = workerProfileFlags

    override fun onAttach(context: Context) {
        super.onAttach(context)

        bottomSheetClickListener = parentFragment as BottomSheetClickListener
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogFragmentWSelectProfileBinding.inflate(inflater, container, false)

        selectProfileFromGallery()

        // 프로필 체크 값 확인
        if (workerProfileFlags == -1) { // 갤러리 선택 상태
            deselectAllCheck()
        } else { // 기본 프로필이 체크가 되어있는 상태라면
            when (workerProfileFlags) {
                1 -> {
                    binding.frameOneIv.visibility = View.VISIBLE
                    binding.ivCurrentProfile.setImageResource(R.drawable.img_profile_w_128_px_1)
                }
                2 -> {
                    binding.frameTwoIv.visibility = View.VISIBLE
                    binding.ivCurrentProfile.setImageResource(R.drawable.img_profile_w_128_px_2)
                }
                3 -> {
                    binding.frameThreeIv.visibility = View.VISIBLE
                    binding.ivCurrentProfile.setImageResource(R.drawable.img_profile_w_128_px_3)
                }
                4 -> {
                    binding.frameFourIv.visibility = View.VISIBLE
                    binding.ivCurrentProfile.setImageResource(R.drawable.img_profile_w_128_px_4)
                }
                5 -> {
                    binding.frameFiveIv.visibility = View.VISIBLE
                    binding.ivCurrentProfile.setImageResource(R.drawable.img_profile_w_128_px_5)
                }
            }
        }


        // 프로필 사진 변경(갤러리)
        binding.tvSelectFromGallery.setOnClickListener {
            val profileIntent = Intent()
            profileIntent.setType("image/*")
            profileIntent.setAction(Intent.ACTION_GET_CONTENT)
            getResult.launch(profileIntent)
        }

        // 프로필 사진 변경(기본)
        binding.frameOne.setOnClickListener(this)
        binding.frameTwo.setOnClickListener(this)
        binding.frameThree.setOnClickListener(this)
        binding.frameFour.setOnClickListener(this)
        binding.frameFive.setOnClickListener(this)

        // 사진 저장하기(추후 서버통신도 여기서 진행!)
        binding.btnSave.setOnClickListener {
            // activity에 값 전달

            // 기본 이미지를 선택했을 때
            if(runningFlags != -1){
                // 기본이미지 저장 서버통신 시작
                val postRequest = DefaultImgRequest("w$runningFlags")
                DefaultImgService(this).tryPostNewPW(postRequest)
            }else{
                // 갤러리 선택 이미지 서버통신 시작
            }

           // val getIvDrawable = binding.ivCurrentProfile.drawable
           // val getIvBitmap = (getIvDrawable as BitmapDrawable).bitmap

           // val getUri = getImageUri(context, getIvBitmap)

          //  bottomSheetClickListener.onItemSelected(getUri)

            // checkState 저장하기
            ApplicationClass.prefs.setInt("workerProfileFlags", runningFlags)

            dismiss()
        }

        // 취소 버튼 클릭
        binding.btnCancel.setOnClickListener {
            dismiss()
        }

        return binding.root
    }

    // 프로필 결과 반환
    fun selectProfileFromGallery() {
        // 프로필 결과 반환
        getResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                try {

                    val uri: Uri? = (it.data)?.data
                    Glide.with(requireContext()).load(uri).circleCrop()
                        .into(binding.ivCurrentProfile)

                    // 플래그 저장 및 기존 Check 전부 비활성화
                    deselectAllCheck()
                    runningFlags = -1

                } catch (e: Exception) {

                }
            } else if (it.resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(requireContext(), "선택 취소", Toast.LENGTH_SHORT).show()
            }
        }
    }

    interface BottomSheetClickListener {
        fun onItemSelected(uri: Uri?)
    }

    fun selectProfileFromDefault(v: View?) {

        val profileList: ArrayList<FrameLayout> = arrayListOf(
            binding.frameOne,
            binding.frameTwo,
            binding.frameThree,
            binding.frameFour,
            binding.frameFive
        ) // 사용자가 클릭하면
        val profileCheckList: ArrayList<ImageView> = arrayListOf(
            binding.frameOneIv,
            binding.frameTwoIv,
            binding.frameThreeIv,
            binding.frameFourIv,
            binding.frameFiveIv
        ) // 반응하는 UI
        val profileDrawable: ArrayList<Int> = arrayListOf(
            R.drawable.img_profile_w_128_px_1,
            R.drawable.img_profile_w_128_px_2,
            R.drawable.img_profile_w_128_px_3,
            R.drawable.img_profile_w_128_px_4,
            R.drawable.img_profile_w_128_px_5
        )

        for (i in 0 until profileList.size) {
            if (profileList[i] == v) {
                profileCheckList[i].visibility = View.VISIBLE
                binding.ivCurrentProfile.setImageResource(profileDrawable[i])
                runningFlags = i + 1
            } else {
                profileCheckList[i].visibility = View.INVISIBLE
            }
        }

    }

    fun getImageUri(inContext: Context?, inImage: Bitmap?): Uri? {
        val bytes = ByteArrayOutputStream()
        if (inImage != null) {
            inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        }
        val path = MediaStore.Images.Media.insertImage(
            inContext?.contentResolver,
            inImage,
            "Title" + " - " + Calendar.getInstance().getTime(),
            null
        )
        return Uri.parse(path)
    }

    override fun onClick(v: View?) {
        binding.apply {
            when (v) {
                frameOne -> {
                    selectProfileFromDefault(v)
                }
                frameTwo -> {
                    selectProfileFromDefault(v)
                }
                frameThree -> {
                    selectProfileFromDefault(v)
                }
                frameFour -> {
                    selectProfileFromDefault(v)
                }
                frameFive -> {
                    selectProfileFromDefault(v)
                }
            }

        }
    }

    fun deselectAllCheck() {
        binding.frameOneIv.visibility = View.INVISIBLE
        binding.frameTwoIv.visibility = View.INVISIBLE
        binding.frameThreeIv.visibility = View.INVISIBLE
        binding.frameFourIv.visibility = View.INVISIBLE
        binding.frameFiveIv.visibility = View.INVISIBLE
    }

    // 기본 이미지 등록 성공(근무자)
    override fun onDefaultImgPostSuccess(response: BaseResponse) {
        Log.d("hellotest",response.message.toString())
    }

    // 기본 이미지 등록 실패(근무자)
    override fun onDefaultImgFailure(message: String) {
        Log.d("byetest",message)
    }

}