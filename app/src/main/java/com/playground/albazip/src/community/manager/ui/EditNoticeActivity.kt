package com.playground.albazip.src.community.manager.ui

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.core.net.toUri
import com.playground.albazip.config.BaseActivity
import com.playground.albazip.databinding.ActivityWriteNoticeBinding
import com.playground.albazip.src.community.common.network.GetReadNoticeFragmentView
import com.playground.albazip.src.community.common.network.GetReadNoticeService
import com.playground.albazip.src.community.common.network.ReadNoticeResponse
import com.playground.albazip.src.community.manager.adapter.PostIVAdapter
import com.playground.albazip.src.community.manager.adapter.PostImgData

class EditNoticeActivity:BaseActivity<ActivityWriteNoticeBinding>(ActivityWriteNoticeBinding::inflate),
    GetReadNoticeFragmentView {

    // 이미지 rv를 담는 코드
    private lateinit var postIVAdapter: PostIVAdapter
    private var postImgList = ArrayList<PostImgData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        GetReadNoticeService(this).tryGetNoticeRead(intent.getIntExtra("noticeId", 0))
        showLoadingDialog(this)

        // 버튼 활성화(수정하는 글이라서 조건은 미리 갖추었기 때문에 true 로 설정)
        binding.tvDone.isEnabled = true
        binding.tvDone.setTextColor(Color.parseColor("#ffc400"))

        // 내용 최소 글자 수 감지
        binding.etContent.addTextChangedListener(object: TextWatcher {
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
        for(i in 0 until response.data.boardInfo.image.size) {
            postImgList.add(PostImgData(response.data.boardInfo.image[i].image_path.toUri()))
        }

        postIVAdapter = PostIVAdapter(postImgList,this@EditNoticeActivity)
        binding.rvPhotoList.adapter = postIVAdapter
    }

    override fun onGetReadNoticeFailure(message: String) {
        dismissLoadingDialog()
    }
}