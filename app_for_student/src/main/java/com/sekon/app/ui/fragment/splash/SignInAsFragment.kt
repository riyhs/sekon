package com.sekon.app.ui.fragment.splash

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.sekon.app.R
import kotlinx.android.synthetic.main.fragment_sign_in_as.*

class SignInAsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_sign_in_as, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bt_im_student.setOnClickListener {
            findNavController().navigate(R.id.action_signInAsFragment_to_signInFragmentStudent)
        }

        bt_im_teacher.setOnClickListener {
            findNavController().navigate(R.id.action_signInAsFragment_to_signInTeacherFragment)
        }

    }

}