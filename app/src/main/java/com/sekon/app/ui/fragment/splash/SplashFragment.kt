package com.sekon.app.ui.fragment.splash

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.sekon.app.R
import com.sekon.app.utils.NetworkInfo.TOKEN_KEY

class SplashFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        onSignInFinished()

        Handler(Looper.getMainLooper()).postDelayed({
            if (onBoardingFinished() && TOKEN_KEY != "token") {
                findNavController().navigate(R.id.action_splashFragment_to_mainActivity)
            } else if (onBoardingFinished()) {
                findNavController().navigate(R.id.action_splashFragment_to_signInFragment)
            } else {
                findNavController().navigate(R.id.action_splashFragment_to_viewPagerFragment)
            }
        }, 1500)
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    @SuppressLint("CommitPrefEdits")
    private fun onBoardingFinished(): Boolean {
        val sharedPref = requireActivity().getSharedPreferences("onBoarding", Context.MODE_PRIVATE)
        return sharedPref.getBoolean("Finished", false)
    }

    private fun onSignInFinished() {
        val sharedPref = requireActivity().getSharedPreferences("onSignIn", Context.MODE_PRIVATE)
        TOKEN_KEY = sharedPref.getString("token", "token").toString()
    }
}