package com.example.albazip.src.register.manager

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.inputmethod.InputMethodManager
import androidx.viewpager2.widget.ViewPager2
import com.example.albazip.R
import com.example.albazip.config.ApplicationClass
import com.example.albazip.config.BaseActivity
import com.example.albazip.data.network.response.SearchPlaceResponse
import com.example.albazip.databinding.ActivitySearchPlaceBinding
import com.example.albazip.src.register.manager.adapter.SearchResultVPAdpater
import com.example.albazip.src.register.manager.data.local.PlaceData
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchPlaceActivity :
    BaseActivity<ActivitySearchPlaceBinding>(ActivitySearchPlaceBinding::inflate) {

    // 검색 결과를 담기 위한 배열 생성
    val itemList = ArrayList<PlaceData>()

    private lateinit var placeAdapter: SearchResultVPAdpater

    private lateinit var marker:MapPOIItem
    private lateinit var mapView:MapView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 뷰페이저 설정
        /* 여백, 너비에 대한 정의 */
        val pagerWidth = resources.getDimensionPixelOffset(R.dimen.pageWidth)
        val screenWidth = resources.displayMetrics.widthPixels
        val pagerPadding = ((screenWidth-pagerWidth)*0.3).toInt() // 아이템의 padding
        val offsetPx = ((screenWidth-pagerWidth)*0.15).toInt() // 아이템 간의 간격

        binding.vpPlace.clipChildren = false
        binding.vpPlace.setPadding(pagerPadding,0,pagerPadding,0)
        binding.vpPlace.setPageTransformer { page, position ->
            page.translationX = position * offsetPx
        }

        binding.vpPlace.offscreenPageLimit = 1 // 몇 개의 페이지를 미리 로드 해둘것인지

        // 페이지 선택 동작 반환 함수
        binding.vpPlace.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {

                // pager 활성화
                for(i in 0 until itemList.size) {
                    itemList[i].flags = i == position
                    placeAdapter.notifyDataSetChanged()
                }

                // 맵 뷰 갱신
                setMapView(position)

                super.onPageSelected(position)
            }
        })

        // 엔터 키로 edittext 입력 받기
        binding.etSearch.setOnKeyListener { v, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {

                // 키보드 내리기
                val imm = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(binding.etSearch.windowToken, 0)

                // 엔터 눌렀을때 행동 -> 키워드로 검색하기
                searchKeyword(binding.etSearch.text.toString())

                return@setOnKeyListener true
            }

            false
        }

        // 맵 뷰 띄우기 (초기화)
        initialMapView()
    }

    // 맵 뷰 띄우기
    private fun initialMapView(){
        mapView = MapView(this)
        val mapViewContainer = binding.rlMapview

        mapViewContainer.addView(mapView)

        // 어댑터 생성
        placeAdapter = SearchResultVPAdpater(itemList)

        marker = MapPOIItem()
        marker.apply {
            itemName = "서울특별시청"   // 마커 이름
            mapPoint = MapPoint.mapPointWithGeoCoord(37.5666805, 126.9784147)
            markerType = MapPOIItem.MarkerType.CustomImage
            customImageResourceId = R.drawable.ic_pin_location
            selectedMarkerType = MapPOIItem.MarkerType.CustomImage
            customSelectedImageResourceId = R.drawable.ic_pin_location
            isCustomImageAutoscale = false
           // setCustomImageAnchor(0.5f, 1.0f)
        }

        // 기본 값은 서울시청
        mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(37.5666805, 126.9784147),true)
        mapView.addPOIItem(marker)
    }


    // 키워드 검색 함수
    private fun searchKeyword(keyword: String) {
        val retrofit = Retrofit.Builder()   // Retrofit 구성
            .baseUrl(ApplicationClass.KAKAO_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val api = retrofit.create(SearchPlaceRetrofitInterface::class.java)   // 통신 인터페이스를 객체로 생성
        val call = api.getSearchKeyword(ApplicationClass.KAKAO_API_KEY, keyword)   // 검색 조건 입력

        // API 서버에 요청
        call.enqueue(object : Callback<SearchPlaceResponse> {
            override fun onResponse(
                call: Call<SearchPlaceResponse>,
                response: Response<SearchPlaceResponse>
            ) {

                // 여기서 view 의 정보를 미리 저장해둔다.

                // itemList 초기화
                itemList.clear()

                // 검색 결과 리스트 itemList 배열에 담기
                for (i in 0 until response.body()!!.documents.size) {
                    //  Log.d("Test", "Body: ${response.body()?.documents?.get(0)?.place_name}")
                    if (i == 0) { // 첫 번째 매장은 검색과 동시에 자동 활성화
                        itemList.add(
                            PlaceData(
                                "${response.body()?.documents?.get(i)?.place_name}",
                                "${response.body()?.documents?.get(i)?.road_address_name}",
                                true,
                                response.body()?.documents?.get(i)?.y!!.toDouble(),
                                response.body()?.documents?.get(i)?.x!!.toDouble()
                            )
                        )
                    } else {
                        itemList.add(
                            PlaceData(
                                "${response.body()?.documents?.get(i)?.place_name}",
                                "${response.body()?.documents?.get(i)?.road_address_name}",
                                false,
                                response.body()?.documents?.get(i)?.y!!.toDouble(),
                                response.body()?.documents?.get(i)?.x!!.toDouble()
                            )
                        )
                    }
                }

                // adapter 와의 연결
                // 만든 어댑터 pager 에 연결(=데이터 갱신)
                binding.vpPlace.adapter = placeAdapter
            }

            override fun onFailure(call: Call<SearchPlaceResponse>, t: Throwable) {
                // 통신 실패
                Log.w("MainActivity", "통신 실패: ${t.message}")
            }

        })
    }

    // 맵 뷰 갱신 -> 근데 이 지도가 바로바로 뜰지가 의문
    private fun setMapView(position:Int){

        // 매장의 중심 좌표 받아오기
        val positionY = itemList[position].latitute
        val positionX = itemList[position].longtitude

        val selectedLMapPoint = MapPoint.mapPointWithGeoCoord(positionY, positionX)

        // 중심좌표 이동
        mapView.setMapCenterPointAndZoomLevel(selectedLMapPoint,0,false)

        // 마커 재설정
        marker.mapPoint = MapPoint.mapPointWithGeoCoord(positionY,positionX)
        marker.itemName = itemList[position].name

    }
}