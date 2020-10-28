package com.sekon.app.ui.fragment.splash

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.sekon.app.R
import com.sekon.app.model.signin.DataSignIn
import com.sekon.app.utils.Preference
import com.sekon.app.viewmodel.SignInViewModel
import kotlinx.android.synthetic.main.fragment_sign_in.*

class SignInFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_sign_in, container, false)
    }

    private lateinit var viewModel : SignInViewModel

    @Suppress("DEPRECATION")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(SignInViewModel::class.java)

        requireActivity().window.navigationBarColor = ContextCompat.getColor(requireContext(), android.R.color.white)
        requireActivity().window.statusBarColor = ContextCompat.getColor(requireContext(), android.R.color.white)
        requireActivity().window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        onBackPressed()
        signIn()
    }

    private fun setupViewModel() {
        viewModel.setSignIn(initData())
        viewModel.getSignInIsSuccess().observe(viewLifecycleOwner, { isSuccess ->
            if (isSuccess == true) {
                viewModel.getToken().observe(viewLifecycleOwner, {token ->
                    saveToken(token)
                })
                viewModel.getSignInResponse().observe(viewLifecycleOwner, {response ->
                    val nama = response.siswa.nama
                    val id = response.siswa._id
                    Preference.saveSiswaName(nama, requireContext())
                    Preference.saveSiswaId(id, requireContext())
                })
                isSignInSuccess(true)
            } else {
                isSignInSuccess(false)
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
            findNavController().navigate(R.id.action_signInFragment_to_mainActivity)
            activity?.finish()
        } else {
            Toast.makeText(requireContext(), "Password / NIS salah", Toast.LENGTH_SHORT).show()
        }
    }

    @SuppressLint("CommitPrefEdits")
    private fun saveToken(token: String) {
        Preference.saveToken(token, requireContext())
    }

    private fun onBackPressed() {
        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                activity?.finish()
            }
        })
    }
}