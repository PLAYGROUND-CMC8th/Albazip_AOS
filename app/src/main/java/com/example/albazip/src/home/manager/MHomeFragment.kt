package com.example.albazip.src.home.manager

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.albazip.R
import com.example.albazip.config.BaseFragment
import com.example.albazip.databinding.FragmentMHomeBinding
import com.example.albazip.src.main.MainActivity
import com.example.albazip.src.mypage.manager.MMyPageFragment
import com.example.albazip.src.schedule.manager.MScheduleFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import org.w3c.dom.Text

class MHomeFragment :
    BaseFragment<FragmentMHomeBinding>(FragmentMHomeBinding::bind, R.layout.fragment_m_home) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}