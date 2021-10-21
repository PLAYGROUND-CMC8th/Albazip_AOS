package com.example.albazip.src.register.manager

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.KeyEvent.KEYCODE_ENTER
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.albazip.config.ApplicationClass.Companion.KAKAO_API_KEY
import com.example.albazip.config.ApplicationClass.Companion.KAKAO_URL
import com.example.albazip.config.BaseActivity
import com.example.albazip.data.network.response.SearchPlaceResponse
import com.example.albazip.databinding.ActivitySearchPlaceBinding
import com.example.albazip.src.register.manager.data.local.PlaceData
import com.example.albazip.src.register.manager.adapter.PlaceListAdapter
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchPlaceActivity :
    BaseActivity<ActivitySearchPlaceBinding>(ActivitySearchPlaceBinding::inflate) {

    private lateinit var pAdapter: PlaceListAdapter

    val resetX: Double = 0.0
    val resetY: Double = 0.0

    // 매장 리스트를 담기 위한 배열 생성
    val itemList = ArrayList<PlaceData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        // 엔터 키로 edittext 입력 받기
        binding.etSearch.setOnKeyListener { v, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KEYCODE_ENTER) {

                // 키보드 내리기
                val imm = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(binding.etSearch.windowToken, 0)

                // 엔터 눌렀을때 행동
                searchKeyword(binding.etSearch.text.toString())

                return@setOnKeyListener true
            }

            false
        }

        // 엔터 눌렀을 때 연결해준다 !


//      // map view 띄우기
        val mapView = MapView(this)
        val mapViewContainer = binding.rlMapview

        mapViewContainer.addView(mapView)

        //val mapPoint = MapPoint.mapPointWithGeoCoord(37.608046,127.061159)
        //
        //mapView.setMapCenterPoint(mapPoint,true)


        //       val marker:MapPOIItem = MapPOIItem()
//        marker.itemName = ""
//        marker.tag = 0
//        marker.isCustomImageAutoscale = true
//        marker.markerType = MapPOIItem.MarkerType.BluePin
//        marker.mapPoint = MapPoint.mapPointWithGeoCoord(126.54587355630036,33.379777816446165)


        //mapView.addPOIItem(marker)

        //mapView.setMapCenterPoint(mapPoint,true)
        //mapView.setZoomLevel(-1,true)


        // 시작 뷰 설정


//        itemList.add(PlaceData("서플라이 가", "광주 광산구 임방울대로826번길 47", false))
//        itemList.add(PlaceData("서플라이 나", "광주 서구 상무자유로 32", false))
//        itemList.add(PlaceData("서플라이 다", "광주 광산구 임방울대로826번길 47", false))
//        itemList.add(PlaceData("서플라이 라", "광주 서구 상무자유로 32", false))
//        itemList.add(PlaceData("서플라이 마", "광주 광산구 임방울대로826번길 47", false))
//        itemList.add(PlaceData("서플라이 바", "광주 서구 상무자유로 32", false))

        pAdapter = PlaceListAdapter(this, itemList)
//
        // 리사이클러 뷰 타입 설정
        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.orientation = LinearLayoutManager.HORIZONTAL

        binding.rvPlace.layoutManager = linearLayoutManager

        pAdapter.itemClick = object : PlaceListAdapter.ItemClick {

            override fun onClick(view: View, position: Int) {

                // 클릭 시 스크롤 이동
                linearLayoutManager.scrollToPositionWithOffset(position, 0)

                for (i in 0 until itemList.size) {
                    itemList[i].flags = i == position
                }

                pAdapter.notifyDataSetChanged()

                showCustomToast("itemList[position].locationX: " + itemList[position].locationX)


                // 지도 view 재갱신
                // val newPoint = MapPoint.mapPointWithGeoCoord(itemList[position].locationX, itemList[position].locationY)
                mapView.setMapCenterPointAndZoomLevel(
                    MapPoint.mapPointWithGeoCoord(
                        37.608046,
                        127.061159
                    ), -1, true
                )
                Log.d(
                    "Test",
                    "x" + itemList[position].locationX.toString() + "y:" + itemList[position].locationY.toString()
                )


                mapView.requestLayout()

//                mapView.zoomIn(true)
//                mapView.zoomOut(true)
//                mapView.requestLayout()
                //setMarker(itemList[position].locationX, itemList[position].locationY)
            }
        }

//        // 만든 어댑터 recyclerview 에 연결
//        binding.rvPlace.adapter = pAdapter

    }

    // 키워드 검색 함수
    private fun searchKeyword(keyword: String) {
        val retrofit = Retrofit.Builder()   // Retrofit 구성
            .baseUrl(KAKAO_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val api = retrofit.create(SearchPlaceRetrofitInterface::class.java)   // 통신 인터페이스를 객체로 생성
        val call = api.getSearchKeyword(KAKAO_API_KEY, keyword)   // 검색 조건 입력

        // API 서버에 요청
        call.enqueue(object : Callback<SearchPlaceResponse> {
            override fun onResponse(
                call: Call<SearchPlaceResponse>,
                response: Response<SearchPlaceResponse>
            ) {
                // 통신 성공 (검색 결과는 response.body()에 담겨있음)
                Log.d("Test", "Raw: ${response.raw()}")

                // itemList 초기화
                itemList.clear()

                for (i in 0 until response.body()!!.documents.size) {
                    //  Log.d("Test", "Body: ${response.body()?.documents?.get(0)?.place_name}")
                    if (i == 0) {
                        itemList.add(
                            PlaceData(
                                "${response.body()?.documents?.get(i)?.place_name}",
                                "${response.body()?.documents?.get(i)?.road_address_name}",
                                true,
                                response.body()?.documents?.get(i)?.x!!.toDouble(),
                                response.body()?.documents?.get(i)?.y!!.toDouble()
                            )
                        )
                    } else {
                        itemList.add(
                            PlaceData(
                                "${response.body()?.documents?.get(i)?.place_name}",
                                "${response.body()?.documents?.get(i)?.road_address_name}",
                                false,
                                response.body()?.documents?.get(i)?.x!!.toDouble(),
                                response.body()?.documents?.get(i)?.y!!.toDouble()
                            )
                        )
                    }
                }

                // adapter 와의 연결
                // 만든 어댑터 recyclerview 에 연결
                binding.rvPlace.adapter = pAdapter

                Log.d("Test", "Body: ${response.body()?.documents?.get(0)?.place_name}")
                Log.d("Test", "Body: ${response.body()?.documents?.get(0)?.address_name}")
                Log.d(
                    "Test",
                    "Body: ${response.body()?.documents?.get(0)?.road_address_name}"
                ) // 이걸로 가자
                Log.d("Test", "Body: ${response.body()?.documents?.get(0)?.x}")
                Log.d("Test", "Body: ${response.body()?.documents?.get(0)?.y}")
            }

            override fun onFailure(call: Call<SearchPlaceResponse>, t: Throwable) {
                // 통신 실패
                Log.w("MainActivity", "통신 실패: ${t.message}")
                showCustomToast("통신실패?")
            }

        })
    }

}