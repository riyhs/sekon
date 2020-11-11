package com.sekon.app.ui.fragment.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.sekon.app.R
import kotlinx.android.synthetic.main.fragment_first_on_boarding.view.*

class FirstOnBoardingFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_first_on_boarding, container, false)
        val viewPager = activity?.findViewById<ViewPager2>(R.id.view_pager_on_boarding)

        view.tv_swipe1.setOnClickListener {
            viewPager?.currentItem = 1
        }

        return view
    }
}