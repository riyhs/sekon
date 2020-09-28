package com.sekon.app.ui.fragment.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.sekon.app.R
import com.sekon.app.adapter.StudyRefAdapter
import com.sekon.app.model.StudyRef
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment() {

    private val list = listOf(
        StudyRef("Google IO", "Google IO adalah acara tahunan yang diselenggarakan oleh Google untuk jogging, yoi mantap!"),
        StudyRef("Google IO", "Google IO adalah acara tahunan yang diselenggarakan oleh Google untuk jogging, yoi mantap!"),
        StudyRef("Google IO", "Google IO adalah acara tahunan yang diselenggarakan oleh Google untuk jogging, yoi mantap!")
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupAdapter()
    }

    private fun setupAdapter() {
        rv_study_ref.setHasFixedSize(true)
        rv_study_ref.layoutManager = LinearLayoutManager(context)
        rv_study_ref.adapter = StudyRefAdapter(list)
        rv_study_ref.isNestedScrollingEnabled = false
    }

}