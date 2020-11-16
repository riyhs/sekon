package com.sekon.app.ui.fragment.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.sekon.app.R
import com.sekon.app.adapter.FeatureAdapter
import com.sekon.app.model.Feature
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_feature.*

class FeatureFragment : Fragment() {

    private val features = listOf(
        Feature(0, "Absen", "Deskripsi"),
        Feature(1, "Jadwal", "Deskripsi"),
        Feature(2, "Saran", "Deskripsi")
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_feature, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupAdapter()
        onBackPressed()
    }

    private fun setupAdapter() {
        rv_feature.setHasFixedSize(true)
        rv_feature.layoutManager = LinearLayoutManager(context)
        rv_feature.adapter = FeatureAdapter(features)
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