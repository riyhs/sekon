package com.sekon.app.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class OnBoardingAdapter (private val list: ArrayList<Fragment>, fm: FragmentManager, lifecycle: Lifecycle) : FragmentStateAdapter(fm ,lifecycle) {
    override fun getItemCount(): Int = list.size

    override fun createFragment(position: Int): Fragment {
        return list[position]
    }

}