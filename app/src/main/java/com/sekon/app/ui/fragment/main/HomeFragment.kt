package com.sekon.app.ui.fragment.main

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.sekon.app.R
import com.sekon.app.adapter.MainCardAdapter
import com.sekon.app.model.StudyRef
import com.sekon.app.model.covid.CovidResponseItem
import com.sekon.app.utils.Preference
import com.sekon.app.viewmodel.CovidViewModel
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment() {

    private val list = listOf(
        StudyRef("Google IO", "Google IO adalah acara tahunan yang diselenggarakan oleh Google untuk jogging, yoi mantap!, yahaha hayuk mabar anjay mabar anjayani, uzumaki bayu hahaha. Text panjang sekali ini huhuhu."),
        StudyRef("Google IO", "Google IO adalah acara tahunan yang diselenggarakan oleh Google untuk jogging, yoi mantap!, yahaha hayuk mabar anjay mabar anjayani, uzumaki bayu hahaha. Text panjang sekali ini huhuhu."),
        StudyRef("Google IO", "Google IO adalah acara tahunan yang diselenggarakan oleh Google untuk jogging, yoi mantap!, yahaha hayuk mabar anjay mabar anjayani, uzumaki bayu hahaha. Text panjang sekali ini huhuhu.")
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    private lateinit var covidViewModel: CovidViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showLoading("covid", true)

        val sharedPref = Preference.initPref(requireContext(), "onSignIn")
        val nameSiswa = sharedPref.getString("name", "name")

        setupAdapter()

        if (isAdded) {
            setupViewModel()
        }

        tv_home_siswa_name.text = nameSiswa
    }

    private fun setupViewModel() {
        covidViewModel = ViewModelProvider(requireActivity()).get(CovidViewModel::class.java)
        covidViewModel.setCovidInfo("2020-10-17T00:00:00Z", "2020-10-18T00:00:00Z")
        covidViewModel.getCovidInfo().observe(requireActivity(), {
            try {
                if (it != null) {
                    setupCovidInfo(it)
                } else {
                    Toast.makeText(context, "Gagal mengambil data COVID-19", Toast.LENGTH_SHORT).show()

                }
            } catch (e: Exception) {
                Log.d("COVID", "Gagal : ${e.message}")
            }
            showLoading("covid", false)
        })
    }

    private fun setupCovidInfo(data: CovidResponseItem) {
        tv_covid_dead.text = data.Deaths.toString()
        tv_covid_health.text = data.Recovered.toString()
        tv_covid_positive.text = data.Active.toString()
    }

    private fun setupAdapter() {
        rv_study_ref.setHasFixedSize(true)
        rv_study_ref.layoutManager = LinearLayoutManager(context)
        rv_study_ref.adapter = MainCardAdapter(list)
        rv_study_ref.isNestedScrollingEnabled = false
    }

    private fun showLoading(name: String, state: Boolean) {
        when (name) {
            "covid" -> {
                if (pb_covid != null) {
                    pb_covid.isVisible = state
                    tv_covid_positive.isGone = state
                    tv_covid_dead.isGone = state
                    tv_covid_health.isGone = state
                }
            }
        }
    }
}
