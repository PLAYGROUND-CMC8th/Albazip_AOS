package com.playground.albazip.src.register.manager.runtime

import androidx.activity.viewModels
import com.playground.albazip.R
import com.playground.albazip.config.BaseViewActivity
import com.playground.albazip.databinding.ActivityRunTimeBinding
import com.playground.albazip.src.register.manager.runtime.viewmodel.RunTimeViewModel

class RunTimeActivity : BaseViewActivity<ActivityRunTimeBinding>(R.layout.activity_run_time) {

    private val runTimeViewModel: RunTimeViewModel by viewModels()


}