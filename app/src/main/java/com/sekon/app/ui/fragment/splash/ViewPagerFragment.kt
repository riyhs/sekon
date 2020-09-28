package com.sekon.app.ui.fragment.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.sekon.app.R
import com.sekon.app.adapter.OnBoardingAdapter
import kotlinx.android.synthetic.main.fragment_view_pager.view.*

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

        view.view_pager_on_boarding.adapter = OnBoardingAdapter(fragmentList, requireActivity().supportFragmentManager, lifecycle)
        return view
    }
}