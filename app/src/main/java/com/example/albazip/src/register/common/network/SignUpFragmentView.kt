package com.example.albazip.src.register.common.network

import com.example.albazip.src.register.common.data.remote.SignUpResponse

interface SignUpFragmentView {

    fun onPostSignUpSuccess(response: SignUpResponse)

    fun onPostSignUpFailure(message: String)
}