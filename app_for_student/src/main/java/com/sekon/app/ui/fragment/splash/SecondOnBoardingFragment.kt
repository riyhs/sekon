package com.sekon.app.ui.fragment.splash

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.sekon.app.R
import com.sekon.app.utils.Preference
import kotlinx.android.synthetic.main.fragment_second_on_boarding.view.*

class SecondOnBoardingFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_second_on_boarding, container, false)

        view.tv_swipe2.setOnClickListener {
            findNavController().navigate(R.id.action_viewPagerFragment_to_signInAsFragment)
            onBoardingFinished()
        }

        return view
    }

    @SuppressLint("CommitPrefEdits")
    private fun onBoardingFinished() {
        Preference.onBoardingFinished(requireContext(), true)
    }
}