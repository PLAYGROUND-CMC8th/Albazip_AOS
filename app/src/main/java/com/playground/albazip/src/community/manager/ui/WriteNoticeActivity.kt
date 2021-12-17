package com.playground.albazip.src.community.manager.ui

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.view.size
import com.playground.albazip.R
import com.playground.albazip.config.BaseActivity
import com.playground.albazip.config.BaseResponse
import com.playground.albazip.databinding.ActivityWriteNoticeBinding
import com.playground.albazip.src.community.manager.adapter.PostIVAdapter
import com.playground.albazip.src.community.manager.adapter.PostImgData
import com.playground.albazip.src.community.manager.network.PostBoardNoticeFragmentView
import com.playground.albazip.src.community.manager.network.PostBoardNoticeService
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.InputStream
import java.lang.Exception

class WriteNoticeActivity:BaseActivity<ActivityWriteNoticeBinding>(ActivityWriteNoticeBinding::inflate),PostBoardNoticeFragmentView {

    private lateinit var postIVAdapter: PostIVAdapter
    private var postImgList = ArrayList<PostImgData>()

    // 프로필 결과 반환
    private val getResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            try {
                it.data?.let {
                    val list = arrayListOf<Uri>()

                    when{
                        it?.data != null -> {
                            list.add(it.data!!)

                            if(binding.rvPhotoList.size < 2) {
                                postImgList.add(PostImgData(list[0]))
                                postIVAdapter = PostIVAdapter(postImgList, this@WriteNoticeActivity)
                                binding.rvPhotoList.adapter = postIVAdapter
                            }else{
                                showCustomToast("최대 두 장까지 업로드 가능합니다!")
                            }

                            postIVAdapter.notifyDataSetChanged()
                        }

                        it?.clipData != null -> {
                            val clip = it?.clipData
                            val size = clip?.itemCount

                            for (i in 0 until size!!){
                                val item = clip.getItemAt(i).uri
                                list.add(item)
                            }

                            if(binding.rvPhotoList.size < 2) {
                                // 리사이클러뷰에 불러온 데이터들 연결하기
                                for (i in 0 until list.size) {
                                    postImgList.add(PostImgData(list[i]))
                                }
                                postIVAdapter = PostIVAdapter(postImgList, this@WriteNoticeActivity)
                                binding.rvPhotoList.adapter = postIVAdapter
                            }else{
                                showCustomToast("최대 두 장까지 업로드 가능합니다!")
                            }

                            postIVAdapter.notifyDataSetChanged()
                        }
                        else -> {
                        }

                    }


                }

            } catch (e: Exception) {

            }
        } else if (it.resultCode == Activity.RESULT_CANCELED) {
            Toast.makeText(this@WriteNoticeActivity, "선택 취소", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 닫기 버튼
        binding.ibtnBack.setOnClickListener {
            finish()
        }

        // 공지사항 업로드 하기
        binding.tvDone.setOnClickListener {

            val imageList = postImgList
            val fileList = ArrayList<MultipartBody.Part>()

            for (i in 0 until imageList.size){
                fileList.add(uriToFilePath(imageList[i].img_path))
            }
           /// val postRequest = PostBoardNoticeRequest(
           //     title = binding.etTitle.text.toString(),
           //     content = binding.etContent.text.toString(),
           // )

            var title = binding.etTitle.text.toString().toRequestBody("text/plain".toMediaTypeOrNull())

            var content = binding.etContent.text.toString().toRequestBody("text/plain".toMediaTypeOrNull())

            PostBoardNoticeService(this).tryPutNoticeReport(title,content,fileList)
            showLoadingDialog(this)
        }

        // 제목 배경 변화 감지
        binding.etTitle.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus)
                binding.clTitle.background = ContextCompat.getDrawable(
                    this,
                    R.drawable.bg_text_field_active
                ) else {
                binding.clTitle.background = ContextCompat.getDrawable(
                    this,
                    R.drawable.bg_text_field_normal
                )
            }
        }

        // 내용 배경 변화 감지
        binding.etContent.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus)
                binding.clContents.background = ContextCompat.getDrawable(
                    this,
                    R.drawable.bg_text_field_active
                ) else {
                binding.clContents.background = ContextCompat.getDrawable(
                    this,
                    R.drawable.bg_text_field_normal
                )
            }
        }

        // 내용 최소 글자 수 감지
        binding.etContent.addTextChangedListener(object:TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.tvTextCnt.text = s?.length.toString() + "자 / 최소20자"
                if (s?.length!! >= 20 && binding.etTitle.text.toString().isNotEmpty()){ // 20자 이상 + 제목이 비어있지 않으면
                    // 버튼 활성화
                    binding.tvDone.isEnabled = true
                    binding.tvDone.setTextColor(Color.parseColor("#ffc400"))
                }else{
                    // 버튼 비활성화
                    binding.tvDone.isEnabled = false
                    binding.tvDone.setTextColor(Color.parseColor("#ADADAD"))
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })

        // 버튼을 누르면 해당 Intent 를 호출하는데,
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "image/*"
            putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        }

        // 이미지 선택(최대 2장)
        binding.clSelectPhotoArea.setOnClickListener {
            // Intent 를 호출 할 때의 Action 은 Intent.ACTION_GET_CONTENT 로 설정하며,
            // extra 에 EXTRA_ALLOW_MULTIPLE 을 true 하는 것을 추가해야한다
            getResult.launch(intent)
            // activity 를 호출 할 때, Chooser 를 이용하여 갤러리 App 을 선택할 수 있도록 한다
            //startActivityForResult(Intent.createChooser(intent, "Chooser Title", GALLERY_REQ_CODE)
        }
    }

    // 공지사항 업로드 성공
    override fun onPostBoardNoticeSuccess(response: BaseResponse) {
        dismissLoadingDialog()
        finish()
    }

    // 업로드 실패
    override fun onPostBoardNoticeFailure(message: String) {
        dismissLoadingDialog()
    }

    // 이미지 파일로 형번환 하는 함수
    fun uriToFilePath(uri: Uri?):MultipartBody.Part {
        val options = BitmapFactory.Options()
        val inputStream: InputStream =
            requireNotNull(this.contentResolver.openInputStream(uri!!))

        var bitmap:Bitmap? = null

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.P){
            val source = ImageDecoder.createSource(this.contentResolver, uri)
            bitmap = ImageDecoder.decodeBitmap(source)
        }else{
            MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
            bitmap = BitmapFactory.decodeStream(inputStream, null, options)
        }

        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap!!.compress(Bitmap.CompressFormat.JPEG, 20, byteArrayOutputStream)
        val fileBody = byteArrayOutputStream.toByteArray()
            .toRequestBody(
                "image/*".toMediaTypeOrNull(),
                0
            )

        val part = MultipartBody.Part.createFormData(
            "images",
            File(uri.toString()).name,
            fileBody
        )

        return part
    }

}