package com.sekon.app.ui.fragment.splash

import android.annotation.SuppressLint
import android.os.Bundle
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
import com.sekon.app.model.signin.DataSignInTeacher
import com.sekon.app.utils.NetworkInfo
import com.sekon.app.utils.Preference
import com.sekon.app.viewmodel.SignInTeacherViewModel
import kotlinx.android.synthetic.main.fragment_sign_in_teacher.*

class SignInTeacherFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_sign_in_teacher, container, false)
    }

    private lateinit var viewModel : SignInTeacherViewModel

    @Suppress("DEPRECATION")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(SignInTeacherViewModel::class.java)

//        requireActivity().window.navigationBarColor = ContextCompat.getColor(requireContext(), android.R.color.white)
        requireActivity().window.statusBarColor = ContextCompat.getColor(requireContext(), android.R.color.white)
        requireActivity().window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

//        onBackPressed()
        signIn()
    }

    private fun setupViewModel() {
        viewModel.setSignIn(initData())
        viewModel.getSignInResponse().observe(viewLifecycleOwner, { response ->
            when (response) {
                is Resource.Success -> {
                    if (response.data != null && response.data.status == "Sukses") {
                        val nama = response.data.guru.nama
                        val id = response.data.guru._id

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
        bt_sign_in_teacher.setOnClickListener {
            setupViewModel()
        }
    }

    private fun initData(): DataSignInTeacher {
        val email = et_sign_in_email_teacher.text.toString()
        val password = et_sign_in_password_teacher.text.toString()
        return DataSignInTeacher(email, password)
    }

    private fun isSignInSuccess(state: Boolean) {
        if (state) {
            onSignInFinished()
            findNavController().navigate(R.id.action_signInTeacherFragment_to_dashboardActivity)
            activity?.finish()
        }
    }

    @SuppressLint("CommitPrefEdits")
    private fun saveToken(token: String) {
        Preference.saveStatus("teacher", requireContext())
        Preference.saveToken(token, requireContext())
    }

    private fun onSignInFinished() {
        val sharedPref = Preference.initPref(requireContext(), "onSignIn")
        NetworkInfo.TOKEN_KEY = sharedPref.getString("token", "token").toString()
    }

    private fun showLoading(state: Boolean) {
        pb_sign_in_teacher.isVisible = state
        ti_sign_in_email_teacher.isInvisible = state
        ti_sign_in_password_teacher.isInvisible = state
        bt_sign_in_teacher.isInvisible = state
        tv_sign_in_teacher.isInvisible = state
        tv_sign_is_as_student.isInvisible = state
    }

    private fun onBackPressed() {
            activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                activity?.finish()
            }
        })
    }
}