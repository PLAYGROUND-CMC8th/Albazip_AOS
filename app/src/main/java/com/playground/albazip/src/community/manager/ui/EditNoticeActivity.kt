package com.playground.albazip.src.community.manager.ui

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.*
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import androidx.core.view.size
import com.playground.albazip.config.BaseActivity
import com.playground.albazip.config.BaseResponse
import com.playground.albazip.databinding.ActivityWriteNoticeBinding
import com.playground.albazip.src.community.common.network.GetReadNoticeFragmentView
import com.playground.albazip.src.community.common.network.GetReadNoticeService
import com.playground.albazip.src.community.common.network.ReadNoticeResponse
import com.playground.albazip.src.community.manager.adapter.PostIVAdapter
import com.playground.albazip.src.community.manager.adapter.PostImgData
import com.playground.albazip.src.community.manager.network.PutEditNoticeFragmentView
import com.playground.albazip.src.community.manager.network.PutEditNoticeService
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.*
import java.net.*


class EditNoticeActivity :
    BaseActivity<ActivityWriteNoticeBinding>(ActivityWriteNoticeBinding::inflate),
    GetReadNoticeFragmentView, PutEditNoticeFragmentView {

    // 이미지 rv를 담는 코드
    private lateinit var postIVAdapter: PostIVAdapter
    private var postImgList = ArrayList<PostImgData>()
    private var noticeId = -1

    private val mHandler = Handler(Looper.getMainLooper())

    // 프로필 결과 반환
    private val getResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                try {
                    it.data?.let {
                        val list = arrayListOf<Uri>()

                        when {
                            it?.data != null -> {
                                list.add(it.data!!)

                                if (binding.rvPhotoList.size < 2) {
                                    postImgList.add(PostImgData(list[0]))
                                    postIVAdapter =
                                        PostIVAdapter(postImgList, this@EditNoticeActivity)
                                    binding.rvPhotoList.adapter = postIVAdapter
                                } else {
                                    showCustomToast("최대 두 장까지 업로드 가능합니다!")
                                }

                                postIVAdapter.notifyDataSetChanged()
                            }

                            it?.clipData != null -> {
                                val clip = it?.clipData
                                val size = clip?.itemCount

                                for (i in 0 until size!!) {
                                    val item = clip.getItemAt(i).uri
                                    list.add(item)
                                }

                                if (binding.rvPhotoList.size < 2) {
                                    // 리사이클러뷰에 불러온 데이터들 연결하기
                                    for (i in 0 until list.size) {
                                        postImgList.add(PostImgData(list[i]))
                                    }
                                    postIVAdapter =
                                        PostIVAdapter(postImgList, this@EditNoticeActivity)
                                    binding.rvPhotoList.adapter = postIVAdapter
                                } else {
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
                Toast.makeText(this@EditNoticeActivity, "선택 취소", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        noticeId = intent.getIntExtra("noticeId", 0)
        GetReadNoticeService(this).tryGetNoticeRead(noticeId)
        showLoadingDialog(this)

        // 버튼 활성화(수정하는 글이라서 조건은 미리 갖추었기 때문에 true 로 설정)
        binding.tvDone.isEnabled = true
        binding.tvDone.setTextColor(Color.parseColor("#ffc400"))

        // 내용 최소 글자 수 감지
        binding.etContent.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.tvTextCnt.text = s?.length.toString() + "자 / 최소20자"
                if (s?.length!! >= 20 && binding.etTitle.text.toString()
                        .isNotEmpty()
                ) { // 20자 이상 + 제목이 비어있지 않으면
                    // 버튼 활성화
                    binding.tvDone.isEnabled = true
                    binding.tvDone.setTextColor(Color.parseColor("#ffc400"))
                } else {
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
            getResult.launch(intent)
        }

        // 공지사항 편집
        binding.tvDone.setOnClickListener {

            val title =
                binding.etTitle.text.toString()
                    .toRequestBody("text/plain".toMediaTypeOrNull())
            val pin = 0.toString().toRequestBody("text/plain".toMediaTypeOrNull())
            val content =
                binding.etContent.text.toString()
                    .toRequestBody("text/plain".toMediaTypeOrNull())

            postIVAdapter.notifyDataSetChanged()
            val imageList = postIVAdapter.itemList
            val fileList = ArrayList<MultipartBody.Part>()


            Thread {
                kotlin.run {

                    for (i in 0 until imageList.size) {

                        if (imageList[i].img_path.toString().contains("https")) {
                            fileList.add(httpToFilePath(imageList[i].img_path.toString())!!)
                        } else {
                            fileList.add(uriToFilePath(imageList[i].img_path))
                        }

                        PutEditNoticeService(this).tryPutEditNotice(
                            noticeId,
                            title,
                            pin,
                            content,
                            fileList
                        )
                        // showLoadingDialog(this)
                    }

                }
            }.start()

        }
    }

    // 작성된  정보 불러오기
    override fun onGetReadNoticeSuccess(response: ReadNoticeResponse) {
        dismissLoadingDialog()
        // 제목
        binding.etTitle.setText(response.data.boardInfo.title)

        // 내용
        binding.etContent.setText(response.data.boardInfo.content)

        // 최소 글자 초기화
        binding.tvTextCnt.text = binding.etContent.text.length.toString() + "자 / 최소20자"

        // 사진
        for (i in 0 until response.data.boardInfo.image.size) {
            postImgList.add(PostImgData(response.data.boardInfo.image[i].image_path.toUri()))
        }

        postIVAdapter = PostIVAdapter(postImgList, this@EditNoticeActivity)
        binding.rvPhotoList.adapter = postIVAdapter
    }

    override fun onGetReadNoticeFailure(message: String) {
        dismissLoadingDialog()
    }

    // 편집성공
    override fun onPutBoardNoticeSuccess(response: BaseResponse) {
        showCustomToast(response.message.toString())
        finish()
    }

    override fun onPutBoardNoticeFailure(message: String) {
        // dismissLoadingDialog()
        showCustomToast(message)
    }

    // 이미지 파일로 형번환 하는 함수
    fun uriToFilePath(uri: Uri?): MultipartBody.Part {
        val options = BitmapFactory.Options()
        val inputStream: InputStream =
            requireNotNull(this.contentResolver.openInputStream(uri!!))
        val bitmap = BitmapFactory.decodeStream(inputStream, null, options)
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

    // 기존 이미지 파일로 형변환
    fun httpToFilePath(strImageURL: String): MultipartBody.Part? {
        val options = BitmapFactory.Options()
        var imgBitmap: Bitmap? = null

        try {
            var url = URL(strImageURL)
            var conn: URLConnection = url.openConnection()
            conn.connect()

            var nSize = conn.contentLength
            var bis: BufferedInputStream = BufferedInputStream(conn.getInputStream(), nSize)
            imgBitmap = BitmapFactory.decodeStream(bis, null, options)

            val byteArrayOutputStream = ByteArrayOutputStream()
            imgBitmap!!.compress(Bitmap.CompressFormat.JPEG, 20, byteArrayOutputStream)
            val fileBody = byteArrayOutputStream.toByteArray()
                .toRequestBody(
                    "image/*".toMediaTypeOrNull(),
                    0
                )

            val part = MultipartBody.Part.createFormData(
                "images",
                strImageURL,
                fileBody
            )

            bis.close()

            return part

        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }

        return null
    }

}
