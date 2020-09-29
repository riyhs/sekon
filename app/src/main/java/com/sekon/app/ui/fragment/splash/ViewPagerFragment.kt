package com.sekon.app.ui.fragment.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.sekon.app.R
import com.sekon.app.adapter.OnBoardingAdapter
import me.relex.circleindicator.CircleIndicator3

class ViewPagerFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_view_pager, container, false)

        val fragmentList = arrayListOf<Fragment>(
            FirstOnBoardingFragment(),
            SecondOnBoardingFragment()
        )

        val viewPager = view.findViewById<ViewPager2>(R.id.view_pager_on_boarding)
        viewPager.adapter = OnBoardingAdapter(fragmentList, requireActivity().supportFragmentManager, lifecycle)

        val indicator = view.findViewById<CircleIndicator3>(R.id.indicator)
        indicator.setViewPager(viewPager)

        return view
    }
}