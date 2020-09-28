package com.sekon.app.ui.fragment.splash

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.sekon.app.MainActivity
import com.sekon.app.R
import kotlinx.android.synthetic.main.fragment_second_on_boarding.view.*

class SecondOnBoardingFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_second_on_boarding, container, false)

        view.tv_swipe2.setOnClickListener {
            startActivity(Intent(requireContext(), MainActivity::class.java))
            activity?.finish()
            onBoardingFinished()
        }

        return view
    }

    @SuppressLint("CommitPrefEdits")
    private fun onBoardingFinished() {
        val sharedPref = requireActivity().getSharedPreferences("onBoarding", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putBoolean("Finished", true)
        editor.apply()
    }
}