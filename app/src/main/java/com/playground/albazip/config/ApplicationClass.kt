package com.playground.albazip.config

import android.app.Application
import com.google.firebase.analytics.FirebaseAnalytics
import com.playground.albazip.util.PreferenceUtil
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ApplicationClass: Application()  {
    companion object {
        const val KAKAO_URL = "https://dapi.kakao.com/"
        const val KAKAO_API_KEY = "KakaoAK de9363698180277fcaae08b7d4bf415c"  // REST API 키

        // const val API_URL = "http://3.140.50.45:3000"  // 테스트 서버(old)
        const val API_URL = "http://3.140.50.45:3000" // 테스트 서버(new)
        // const val API_URL = "http://18.222.46.235:3000" // 실서버

        // Retrofit 인스턴스, 앱 실행시 한번만 생성하여 사용합니다.
        lateinit var sRetrofit: Retrofit

        // sharedPreference : 초기 정보를 받기 위한 prefs 생성
        lateinit var prefs: PreferenceUtil

        // firebaseAnalytics
        lateinit var firebaseAnalytics: FirebaseAnalytics

        // JWT Token Header 키 값
        val X_ACCESS_TOKEN = "X-ACCESS-TOKEN"

    }

    override fun onCreate() {
        super.onCreate()

        // sharedPreference
        prefs = PreferenceUtil(applicationContext)

        // 레트로핏 인스턴스 생성
        initRetrofitInstance()

        // firebaseAnalytics
        firebaseAnalytics = FirebaseAnalytics.getInstance(this)

    }

    // 레트로핏 인스턴스를 생성하고, 레트로핏에 각종 설정값들을 지정해줍니다.
    // 연결 타임아웃시간은 5초로 지정이 되어있고, HttpLoggingInterceptor를 붙여서 어떤 요청이 나가고 들어오는지를 보여줍니다.
    private fun initRetrofitInstance() {
        val client: OkHttpClient = OkHttpClient.Builder()
            .readTimeout(100000, TimeUnit.MILLISECONDS)
            .connectTimeout(100000, TimeUnit.MILLISECONDS)
            // 로그캣에 okhttp.OkHttpClient로 검색하면 http 통신 내용을 보여줍니다.
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .addNetworkInterceptor(XAccessTokenInterceptor()) // JWT 자동 헤더 전송
            .build()

        // sRetrofit 이라는 전역변수에 API url, 인터셉터, Gson을 넣어주고 빌드해주는 코드
        // 이 전역변수로 http 요청을 서버로 보내면 됩니다.
        sRetrofit = Retrofit.Builder()
            .baseUrl(API_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}