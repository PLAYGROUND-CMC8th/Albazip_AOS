package com.playground.albazip.src.community.common.ui

import android.animation.ValueAnimator
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.core.view.get
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.playground.albazip.R
import com.playground.albazip.config.BaseActivity
import com.playground.albazip.databinding.ActivityPhotoListBinding
import com.playground.albazip.src.community.common.adapter.ImgListVPAdapter


class PhotoListActivity:BaseActivity<ActivityPhotoListBinding>(ActivityPhotoListBinding::inflate) {

    private lateinit var imgVPAdapter: ImgListVPAdapter

        override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        // 시스템바 색상 변경
        window.statusBarColor = Color.parseColor("#000000")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 닫기 버튼
        binding.btnDel.setOnClickListener {
            finish()
        }

        val imgList = intent.getSerializableExtra("imgList") as ArrayList<String>

        imgVPAdapter = ImgListVPAdapter(this@PhotoListActivity,imgList.distinct())
        binding.vpImgList.adapter = imgVPAdapter


        // wrap_content 로 원본 크기의 사진 유지하기
        binding.vpImgList.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                // 몇 번 째 사진인지 top bar 표시
                binding.tvPageCntTitle.text  = (position+1).toString()+"/"+imgVPAdapter.itemCount.toString()

                val view = (binding.vpImgList[0] as RecyclerView).layoutManager?.findViewByPosition(position)
                view?.post {
                    val wMeasureSpec = View.MeasureSpec.makeMeasureSpec(view.width, View.MeasureSpec.EXACTLY)
                    val hMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
                    view.measure(wMeasureSpec, hMeasureSpec)
                    if (binding.vpImgList.layoutParams.height != view.measuredHeight) {
                        binding.vpImgList.layoutParams = (binding.vpImgList.layoutParams).also { lp -> lp.height = view.measuredHeight }
                    }
                }
            }
        })

        // 사진만 보기 (toolbar 잠시 제거)
        imgVPAdapter.setOnItemClickListener(object:ImgListVPAdapter.OnItemClickListener{
            override fun onItemClick(v: View, position: Int) {
                if(binding.flTop.visibility == View.INVISIBLE){ // 안 보이는 상태면 보이게 변경
                    binding.flTop.visibility = View.VISIBLE
                    binding.rlTopItem.visibility = View.VISIBLE
                }else{
                    binding.flTop.visibility = View.INVISIBLE
                    binding.rlTopItem.visibility = View.INVISIBLE
                }
            }
        })

    }
}