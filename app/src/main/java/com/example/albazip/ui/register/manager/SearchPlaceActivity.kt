package com.example.albazip.ui.register.manager

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.albazip.config.BaseActivity
import com.example.albazip.databinding.ActivitySearchPlaceBinding
import com.example.albazip.ui.register.manager.adapter.PlaceListAdapter

class SearchPlaceActivity :
    BaseActivity<ActivitySearchPlaceBinding>(ActivitySearchPlaceBinding::inflate) {

    private lateinit var pAdapter: PlaceListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 매장 리스트를 담기 위한 배열 생성
        val itemList = ArrayList<PlaceData>()

        itemList.add(PlaceData("서플라이 가", "광주 광산구 임방울대로826번길 47",false))
        itemList.add(PlaceData("서플라이 나", "광주 서구 상무자유로 32",false))
        itemList.add(PlaceData("서플라이 다", "광주 광산구 임방울대로826번길 47",false))
        itemList.add(PlaceData("서플라이 라", "광주 서구 상무자유로 32",false))
        itemList.add(PlaceData("서플라이 마", "광주 광산구 임방울대로826번길 47",false))
        itemList.add(PlaceData("서플라이 바", "광주 서구 상무자유로 32",false))

        pAdapter = PlaceListAdapter(this, itemList)

        // 리사이클러 뷰 타입 설정
        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.orientation = LinearLayoutManager.HORIZONTAL

        binding.rvPlace.layoutManager = linearLayoutManager

        pAdapter.itemClick = object : PlaceListAdapter.ItemClick {

            override fun onClick(view: View, position: Int) {

                // 클릭 시 스크롤 이동
                linearLayoutManager.scrollToPositionWithOffset(position, 0)

                for(i in 0 until itemList.size){
                    itemList[i].flags = i == position
                }

                pAdapter.notifyDataSetChanged()
            }
        }

        // 만든 어댑터 recyclerview 에 연결
        binding.rvPlace.adapter = pAdapter

    }

}