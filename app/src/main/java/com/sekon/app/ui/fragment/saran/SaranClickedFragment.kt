package com.sekon.app.ui.fragment.saran

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.sekon.app.R
import com.sekon.app.model.saran.SaranResponseDetail
import kotlinx.android.synthetic.main.fragment_saran_clicked.*

class SaranClickedFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_saran_clicked, container, false)
    }

    private val args: SaranClickedFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val saran = args.saran

        setupLayout(saran)

    }

    private fun setupLayout(saran: SaranResponseDetail) {
        Glide
            .with(requireContext())
            .load(saran.photo)
            .into(iv_saran_detail)

        tv_saran_detail_title.text = saran.saran
        tv_saran_detail_desc.text = saran.deskripsi
    }
}