package com.example.albazip.ui.login

import android.graphics.Point
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.albazip.R
import com.example.albazip.config.BaseActivity
import com.example.albazip.databinding.ActivityRegisterBinding
import com.example.albazip.ui.register.AgreementFragment
import com.example.albazip.ui.register.InputInfoFragment
import com.example.albazip.ui.register.InputPWFragment
import com.example.albazip.ui.register.InputPhoneFragment

class RegisterActivity : BaseActivity<ActivityRegisterBinding>(ActivityRegisterBinding::inflate) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportFragmentManager.beginTransaction().replace(R.id.main_fragment, AgreementFragment()).commitAllowingStateLoss()

    }
}