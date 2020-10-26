package com.sekon.app.ui.fragment.main

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isGone
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.sekon.app.R
import com.sekon.app.adapter.MainCardAdapter
import com.sekon.app.model.Resource
import com.sekon.app.model.covid.CovidResponseItem
import com.sekon.app.utils.Preference
import com.sekon.app.viewmodel.CovidViewModel
import com.sekon.app.viewmodel.ReferenceViewModel
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    private lateinit var covidViewModel: CovidViewModel
    private lateinit var referenceViewModel: ReferenceViewModel
    private lateinit var selectedChip: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        covidViewModel = ViewModelProvider(requireActivity()).get(CovidViewModel::class.java)
        referenceViewModel = ViewModelProvider(requireActivity()).get(ReferenceViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showLoading("covid", true)

        val sharedPref = Preference.initPref(requireContext(), "onSignIn")
        val nameSiswa = sharedPref.getString("name", "name")
        tv_home_siswa_name.text = nameSiswa

        setupAdapter()
        chipOnClickListener()

        selectedChip = "rpl"

        if (isAdded) {
            setupCovidViewModel()
            setupReferenceViewModel(selectedChip)
        }
    }

    private fun setupCovidViewModel() {
        covidViewModel.setCovidInfo("2020-10-17T00:00:00Z", "2020-10-18T00:00:00Z")
        covidViewModel.getCovidInfo().observe(viewLifecycleOwner, {
            try {
                if (it != null) {
                    setupCovidInfo(it)
                } else {
                    Toast.makeText(context, "Gagal mengambil data COVID-19", Toast.LENGTH_SHORT)
                        .show()

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

    private fun setupReferenceViewModel(kelas: String) {
        referenceViewModel.setReference(kelas)
        referenceViewModel.getReference().observe(viewLifecycleOwner, { response ->
            when (response) {
                is Resource.Success -> {
                    response.data?.let {
                        rv_study_ref.adapter = MainCardAdapter(it)
                        showLoading("referensi", false)
                    }
                }
                is Resource.Error -> {
                    response.message?.let {
                        Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                    }
                }

                is Resource.Loading -> {
                    showLoading("referensi", true)
                }
            }
        })
    }

    private fun setupAdapter() {
        rv_study_ref.setHasFixedSize(true)
        rv_study_ref.layoutManager = LinearLayoutManager(context)
        rv_study_ref.isNestedScrollingEnabled = false
    }

    private fun chipOnClickListener() {
        chip_rpl.setOnClickListener {
            selectedChip = "rpl"
            setupReferenceViewModel(selectedChip)
        }
        chip_tbsm.setOnClickListener {
            selectedChip = "tbsm"
            setupReferenceViewModel(selectedChip)
        }
        chip_tei.setOnClickListener {
            selectedChip = "tei"
            setupReferenceViewModel(selectedChip)
        }
        chip_tkj.setOnClickListener {
            selectedChip = "tkj"
            setupReferenceViewModel(selectedChip)
        }
        chip_tkro.setOnClickListener {
            selectedChip = "tkr"
            setupReferenceViewModel(selectedChip)
        }
        chip_tp.setOnClickListener {
            selectedChip = "tp"
            setupReferenceViewModel(selectedChip)
        }
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
            "referensi" -> {
                if (pb_referensi != null) {
                    pb_referensi.isVisible = state
                    rv_study_ref.isInvisible = state
                }
            }
        }
    }
}
