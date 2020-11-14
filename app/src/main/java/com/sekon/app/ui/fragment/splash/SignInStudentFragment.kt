package com.sekon.app.ui.fragment.splash

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.sekon.app.R
import com.sekon.app.model.Resource
import com.sekon.app.model.signin.DataSignIn
import com.sekon.app.utils.NetworkInfo
import com.sekon.app.utils.Preference
import com.sekon.app.viewmodel.SignInViewModel
import kotlinx.android.synthetic.main.fragment_sign_in_student.*

class SignInStudentFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_sign_in_student, container, false)
    }

    private lateinit var viewModel : SignInViewModel

    @Suppress("DEPRECATION")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(SignInViewModel::class.java)

        requireActivity().window.statusBarColor = ContextCompat.getColor(requireContext(), android.R.color.white)
        requireActivity().window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        signIn()
    }

    private fun setupViewModel() {
        viewModel.setSignIn(initData())
        viewModel.getSignInResponse().observe(viewLifecycleOwner, { response ->
            when (response) {
                is Resource.Success -> {
                    if (response.data != null && response.data.status == "sukses") {
                        val nama = response.data.siswa.nama
                        val id = response.data.siswa._id

                        Preference.saveName(nama, requireContext())
                        Preference.saveId(id, requireContext())
                        saveToken(response.data.token)
                        Toast.makeText(context, response.data.message, Toast.LENGTH_SHORT).show()
                        isSignInSuccess(true)
                        showLoading(false)
                    } else {
                        showLoading(false)
                        Toast.makeText(activity, response.data?.message, Toast.LENGTH_SHORT).show()
                    }
                }

                is Resource.Loading -> {
                    showLoading(true)
                }

                is Resource.Error -> {
                    showLoading(false)
                    Toast.makeText(activity, response.message, Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    private fun signIn() {
        bt_sign_in.setOnClickListener {
            setupViewModel()
        }
    }

    private fun initData(): DataSignIn {
        val nis = etSignInNis.text.toString().toInt()
        val password = etSignInPassword.text.toString()
        return DataSignIn(nis, password)
    }

    private fun isSignInSuccess(state: Boolean) {
        if (state) {
            onSignInFinished()
            findNavController().navigate(R.id.action_signInStudentFragment_to_mainActivity)
            activity?.finish()
        }
    }

    @SuppressLint("CommitPrefEdits")
    private fun saveToken(token: String) {
        Preference.saveStatus("student", requireContext())
        Preference.saveToken(token, requireContext())
    }

    private fun onSignInFinished() {
        val sharedPref = Preference.initPref(requireContext(), "onSignIn")
        NetworkInfo.TOKEN_KEY = sharedPref.getString("token", "token").toString()
    }

    private fun showLoading(state: Boolean) {
        pb_sign_in.isVisible = state
        tiSignInNis.isInvisible = state
        tiSignInPassword.isInvisible = state
        bt_sign_in.isInvisible = state
        tv_sign_in.isInvisible = state
    }

    private fun onBackPressed() {
            activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                activity?.finish()
            }
        })
    }
}