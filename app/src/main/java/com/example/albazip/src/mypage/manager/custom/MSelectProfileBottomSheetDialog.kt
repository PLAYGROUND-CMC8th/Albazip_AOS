package com.example.albazip.src.mypage.manager.custom

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
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
import com.example.albazip.src.mypage.common.profile.data.DefaultImgRequest
import com.example.albazip.src.mypage.common.profile.network.DefaultImgFragmentView
import com.example.albazip.src.mypage.common.profile.network.DefaultImgService
import com.example.albazip.src.mypage.common.profile.network.GalleryImgFragmentView
import com.example.albazip.src.mypage.common.profile.network.GalleryImgService
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.InputStream
import java.util.*


class MSelectProfileBottomSheetDialog(context: Context) : BottomSheetDialogFragment(), View.OnClickListener,DefaultImgFragmentView,GalleryImgFragmentView {

    private val mycontext = context

    // 프로필 사진 intent
    private lateinit var getResult: ActivityResultLauncher<Intent>
    private lateinit var binding: DialogFragmentMSelectProfileBinding
    private lateinit var bottomSheetClickListener: BottomSheetClickListener

    // 프로필 체크 Flags 받아오기
    val managerProfileFlags = ApplicationClass.prefs.getInt("managerProfileFlags", 2)
    var runningFlags: Int = managerProfileFlags

    // 갤러리에서 받아온 이미지 uri
    private var galleryUri:Uri? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)

        bottomSheetClickListener = parentFragment as BottomSheetClickListener
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogFragmentMSelectProfileBinding.inflate(inflater, container, false)

        selectProfileFromGallery()

        // 프로필 체크 값 확인
        if (managerProfileFlags == -1) { // 갤러리 선택 상태
            deselectAllCheck()
        } else { // 기본 프로필이 체크가 되어있는 상태라면
            when (managerProfileFlags) {
                1 -> {
                    binding.frameOneIv.visibility = View.VISIBLE
                    binding.ivCurrentProfile.setImageResource(R.drawable.img_profile_m_128_px_3)
                }
                2 -> {
                    binding.frameTwoIv.visibility = View.VISIBLE
                    binding.ivCurrentProfile.setImageResource(R.drawable.img_profile_m_128_px_2)
                }
                3 -> {
                    binding.frameThreeIv.visibility = View.VISIBLE
                    binding.ivCurrentProfile.setImageResource(R.drawable.img_profile_m_128_px_1)
                }
                4 -> {
                    binding.frameFourIv.visibility = View.VISIBLE
                    binding.ivCurrentProfile.setImageResource(R.drawable.img_profile_m_128_px_5)
                }
                5 -> {
                    binding.frameFiveIv.visibility = View.VISIBLE
                    binding.ivCurrentProfile.setImageResource(R.drawable.img_profile_m_128_px_4)
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
            // 기본 이미지를 선택했을 때
            if(runningFlags != -1){
                // 기본이미지 저장 서버통신 시작
                val postRequest = DefaultImgRequest("m$runningFlags")
                DefaultImgService(this).tryPostNewPW(postRequest)
            }else{
                // 갤러리 선택 이미지 서버통신 시작
                uriToFilePath(galleryUri)
            }

            // checkState 저장하기
            ApplicationClass.prefs.setInt("managerProfileFlags", runningFlags)

            // 이전 액티비티에 값 전달
            bottomSheetClickListener.onItemSelected(binding.ivCurrentProfile)

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

                    galleryUri = uri
                    Log.d("galleryUri",galleryUri.toString())

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

    fun uriToFilePath(uri: Uri?) {
        val options = BitmapFactory.Options()
        val inputStream: InputStream =
            requireNotNull(mycontext.contentResolver.openInputStream(uri!!))
        val bitmap = BitmapFactory.decodeStream(inputStream, null, options)
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap!!.compress(Bitmap.CompressFormat.JPEG, 20, byteArrayOutputStream)
        val fileBody = byteArrayOutputStream.toByteArray()
            .toRequestBody(
                "image/jpeg".toMediaTypeOrNull(),
                0
            )

        val part = MultipartBody.Part.createFormData(
            "uploadImage",
            File(uri.toString()).name,
            fileBody
        )

        // 갤러리 업로드 서버통신
        GalleryImgService(this).tryPostGalleryImg(part)
    }

    interface BottomSheetClickListener {
        fun onItemSelected(iv: ImageView)
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
            R.drawable.img_profile_m_128_px_3,
            R.drawable.img_profile_m_128_px_2,
            R.drawable.img_profile_m_128_px_1,
            R.drawable.img_profile_m_128_px_5,
            R.drawable.img_profile_m_128_px_4
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

    // 기본 이미지 업로드
    override fun onDefaultImgPostSuccess(response: BaseResponse) {
        Log.d("hellotest",response.message.toString())
    }

    override fun onDefaultImgFailure(message: String) {
        Log.d("byetest",message)
    }

    // 갤러리 이미지 업로드
    override fun onGalleryImgPostSuccess(response: BaseResponse) {
        Log.d("hellotest",response.message.toString())
    }

    override fun onGalleryImgFailure(message: String) {
        Log.d("byetest",message)
    }

}