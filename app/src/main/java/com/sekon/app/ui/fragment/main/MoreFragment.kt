package com.sekon.app.ui.fragment.main

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.sekon.app.R
import com.sekon.app.model.SiswaResponseDetail
import com.sekon.app.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_more.*

class MoreFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_more, container, false)
    }

    private lateinit var mainViewModel: MainViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewModel()
        onBackPressed()
    }

    private fun setupViewModel() {
        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        mainViewModel.getSiswaDetail().observe(viewLifecycleOwner, {
            if (it != null) {
                setupLayout(it.result)
            } else {
                Toast.makeText(context, "Tidak dapat mengambil data siswa", Toast.LENGTH_SHORT).show()
            }
        })
    }

    @SuppressLint("SetTextI18n")
    private fun setupLayout(siswa: SiswaResponseDetail) {
        tv_more_siswa_name.text = siswa.nama
        tv_more_siswa_tagline.text = "\"${siswa.tagline}\""
        tv_more_siswa_nis.text = siswa.nis.toString()
        tv_more_siswa_kelas.text = siswa.kelas
    }

    private fun onBackPressed() {
        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                activity?.supportFragmentManager
                    ?.beginTransaction()
                    ?.replace(R.id.frame_container, HomeFragment(), HomeFragment::class.java.simpleName)
                    ?.commit()

                activity?.bottomNavigationView?.selectedItemId = R.id.bottom_nav_home
            }
        })
    }

}