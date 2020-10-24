package com.sekon.app.ui.fragment.splash

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.sekon.app.R
import com.sekon.app.model.signin.DataSignIn
import com.sekon.app.model.signin.SignInResponse
import com.sekon.app.network.NetworkConfig
import kotlinx.android.synthetic.main.fragment_sign_in.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignInFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_sign_in, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onBackPressed()
        signIn()
    }

    private fun signIn() {
        bt_sign_in.setOnClickListener {
            val dataSignIn = initData()
            postSignIn(dataSignIn)
        }
    }

    private fun initData(): DataSignIn {
        val nis = etSignInNis.text.toString().toInt()
        val password = etSignInPassword.text.toString()
        return DataSignIn(nis, password)
    }

    private fun postSignIn(data: DataSignIn) {
        NetworkConfig()
            .getService()
            .postSignIn(data)
            .enqueue(object : Callback<SignInResponse> {
                override fun onResponse(
                    call: Call<SignInResponse>,
                    response: Response<SignInResponse>
                ) {
                    isDataValid(response)
                }

                override fun onFailure(call: Call<SignInResponse>, t: Throwable) {
                    Toast.makeText(requireContext(), "Gagal login", Toast.LENGTH_SHORT).show()
                }

            })
    }

    private fun isDataValid(response: Response<SignInResponse>) {
        if (response.body()?.status == "Sukses") {
            Toast.makeText(requireContext(), "success", Toast.LENGTH_SHORT).show()
            onSignInFinished(response.body()?.token.toString())
            isSignInSuccess(true)
        } else {
            Toast.makeText(requireContext(), "NIS / Password salah", Toast.LENGTH_SHORT).show()
        }
    }

    private fun isSignInSuccess(state: Boolean) {
        if (state) {
            findNavController().navigate(R.id.action_signInFragment_to_mainActivity)
            activity?.finish()
        } else {
            Toast.makeText(requireContext(), "Pastikan mempunyai koneksi", Toast.LENGTH_SHORT).show()
        }
    }

    @SuppressLint("CommitPrefEdits")
    private fun onSignInFinished(token: String) {
        val sharedPref = requireActivity().getSharedPreferences("onSignIn", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putString("token", token)
        editor.apply()
    }

    private fun onBackPressed() {
        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                activity?.finish()
            }
        })
    }
}