package com.sekon.app.adapter

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.sekon.app.FeatureFragment

class OnBoardingAdapter (activity: AppCompatActivity, private val itemsCount: Int) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int = itemsCount

    override fun createFragment(position: Int): Fragment {
        return FeatureFragment()
    }

}