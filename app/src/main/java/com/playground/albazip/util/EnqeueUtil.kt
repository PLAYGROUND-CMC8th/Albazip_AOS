package com.playground.albazip.util

import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/** 반복되는 interface 생성을 줄이기 위해 생성한 Call 클래스의 확장함수
 * API 통신 관련 함수를 선언했던 [(도메인명)RetrofitInterface] 가 사라진 대신, 그 역할을 [(도메인명)Service] 가 대신한다.
 * 또한, [(도메인명)Interface] 역시 사라졌기 때문에 Activity 나 Fragment 에 더이상 함수를 override 할 필요없이 이 함수(= enqueueUtil)를 호출하여 바로 서버 작업이 가능하다.
 * 코틀린은 고차함수를 지원하기 때문에 람다를 통해 함수를 매개변수로 전달할 수 있다. 상황에 따라 필요한 인자를 불러와서 UI 로직을 처리하도록 한다.
 */
fun <ResponseType> Call<ResponseType>.enqueueUtil(
    getResultCode: (ResponseType) -> Int, // 기본적인 분기처리를 미리 해놓기 위해 함수 호출시 매번 선언이 필요하다
    onSuccess200: (ResponseType) -> Unit, // 1000: 작업 성공 코드를 호출 할 때 사용하세요
    /** UI에 error 로직을 구현할 필요가 없을 때를 대비해 기본값으로 null 을 넣어주었다.*/
    onError200: ((ResponseType) -> Unit)? = null, // 2000 번대 오류를 호출 할 때 사용하세요
    onError400: ((ResponseType) -> Unit)? = null, // 3000 번대 오류를 호출할 때 사용하세요
    onErrorElse: ((ResponseType) -> Unit)? = null // 4000 번대 오류를 호출할 때 사용하세요
) {
    // 확장된 부분에선 확장이 정의될 자기 자신을 this 로 접근이 가능하다. (여기서는 enqueue 를 확장한 것)
    this.enqueue(object : Callback<ResponseType> {
        // 서버 통신 성공
        override fun onResponse(call: Call<ResponseType>, response: Response<ResponseType>) {

            // 반환 코드를 판단하기 위해 받아오는 code 변수
            val resultCode = getResultCode.invoke(response.body() ?: return)

            when (resultCode) {
                // 작업 성공
                200 -> onSuccess200.invoke(response.body() ?: return)

                // 클라이언트 input 에러
                202 -> onError200?.invoke(response.body() ?: return)

                // db의 의미적 validation 에러
                400 -> onError400?.invoke(response.body() ?: return)

                // 서버 내부 오류
                else -> onErrorElse?.invoke(response.body() ?: return)
            }

        }

        // 서버 통신 실패
        override fun onFailure(call: Call<ResponseType>, t: Throwable) {
            Log.d("NetworkTest", "error:$t")
        }

    })
}