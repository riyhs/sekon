package com.sekon.app.ui.fragment.splash

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.sekon.app.R
import kotlinx.android.synthetic.main.fragment_sign_in_as.*

@Suppress("DEPRECATION")
class SignInAsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_sign_in_as, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        requireActivity().window.navigationBarColor = ContextCompat.getColor(requireContext(), R.color.colorAccent)
        requireActivity().window.statusBarColor = ContextCompat.getColor(requireContext(), R.color.colorAccent)
        requireActivity().window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE

        bt_im_student.setOnClickListener {
            findNavController().navigate(R.id.action_signInAsFragment_to_signInFragmentStudent)
        }

        bt_im_teacher.setOnClickListener {
            findNavController().navigate(R.id.action_signInAsFragment_to_signInTeacherFragment)
        }

        onBackPressed()
    }

    private fun onBackPressed() {
        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                activity?.finish()
            }
        })
    }

}