package com.sekon.app.ui.fragment.splash

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.sekon.app.R
import com.sekon.app.utils.NetworkInfo.STATUS
import com.sekon.app.utils.NetworkInfo.TOKEN_KEY
import com.sekon.app.utils.Preference

class SplashFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        onSignInFinished()
        Handler(Looper.getMainLooper()).postDelayed({
            if (onBoardingFinished() && TOKEN_KEY != "token") {
                if (STATUS == "student") {
                    findNavController().navigate(R.id.action_splashFragment_to_mainActivity)
                    activity?.finish()
                } else {
                    findNavController().navigate(R.id.action_splashFragment_to_dashboardActivity)
                    activity?.finish()
                }
            } else if (onBoardingFinished()) {
                findNavController().navigate(R.id.action_splashFragment_to_signInAsFragment)
            } else {
                findNavController().navigate(R.id.action_splashFragment_to_viewPagerFragment)
            }
        }, 1500)
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    @SuppressLint("CommitPrefEdits")
    private fun onBoardingFinished(): Boolean {
        val sharedPref = Preference.initPref(requireContext(), "onBoarding")
        return sharedPref.getBoolean("Finished", false)
    }

    private fun onSignInFinished() {
        val sharedPref = Preference.initPref(requireContext(), "onSignIn")
        TOKEN_KEY = sharedPref.getString("token", "token").toString()
        STATUS = sharedPref.getString("status", "status").toString()
    }
}