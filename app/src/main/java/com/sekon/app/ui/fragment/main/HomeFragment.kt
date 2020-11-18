package com.sekon.app.ui.fragment.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.sekon.app.R
import com.sekon.app.adapter.MainCardAdapter
import com.sekon.app.adapter.decoration.MarginItemDecorationHorizontal
import com.sekon.app.model.Resource
import com.sekon.app.model.covid.CovidResponseItem
import com.sekon.app.model.reference.ReferenceResponseItem
import com.sekon.app.ui.activity.AbsenActivity
import com.sekon.app.ui.activity.JadwalActivity
import com.sekon.app.ui.activity.SaranActivity
import com.sekon.app.utils.Preference
import com.sekon.app.viewmodel.CovidViewModel
import com.sekon.app.viewmodel.MainViewModel
import com.sekon.app.viewmodel.ReferenceViewModel
import kotlinx.android.synthetic.main.fragment_home.*
import java.util.*

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    private lateinit var covidViewModel: CovidViewModel
    private lateinit var referenceViewModel: ReferenceViewModel
    private lateinit var mainViewModel: MainViewModel
    private lateinit var selectedChip: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        covidViewModel = ViewModelProvider(requireActivity()).get(CovidViewModel::class.java)
        referenceViewModel = ViewModelProvider(requireActivity()).get(ReferenceViewModel::class.java)
        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupAdapter()
        rv_study_ref.adapter = MainCardAdapter(listOf(ReferenceResponseItem(0, "", "", "", "", "")))

        showLoading("covid", true)

        val nameSiswa = getSiswaName()
        tv_home_siswa_name.text = nameSiswa

        val from = getCurrentDate(5)
        val to = getCurrentDate(0)

        chipOnClickListener()
        menuOnClick()

        selectedChip = "rpl"

        if (isAdded) {
            setupCovidViewModel(from, to)
            setupReferenceViewModel(selectedChip)
            setupMainViewModel(getSiswaId())
        }
    }

    private fun setupMainViewModel(id: String) {
        mainViewModel.setSiswaDetail(id)
        mainViewModel.getSiswaDetail().observe(viewLifecycleOwner, {
            val siswa = it.result

            Glide
                .with(requireContext())
                .load(siswa.photo)
                .centerCrop()
                .into(iv_mini_profile)
        })
    }

    private fun menuOnClick() {
        card_menu_absen.setOnClickListener {
            val intent = Intent(activity, AbsenActivity::class.java)
            startActivity(intent)
        }

        card_menu_jadwal.setOnClickListener {
            val intent = Intent(activity, JadwalActivity::class.java)
            startActivity(intent)
        }

        card_menu_saran.setOnClickListener {
            val intent = Intent(activity, SaranActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupCovidViewModel(from: String, to: String) {
        covidViewModel.setCovidInfo(from, to)
        covidViewModel.getCovidInfo().observe(viewLifecycleOwner, {
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

    private fun setupReferenceViewModel(kelas: String) {
        referenceViewModel.setReference(kelas)
        referenceViewModel.getReference().observe(viewLifecycleOwner, { response ->
            when (response) {
                is Resource.Success -> {
                    response.data?.let {
                        if (rv_study_ref != null) {
                            rv_study_ref.adapter = MainCardAdapter(it)
                            showLoading("referensi", false)
                        }
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
        val layoutManager = LinearLayoutManager(context)
        val margin = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16f, resources.displayMetrics)

        layoutManager.orientation = LinearLayoutManager.HORIZONTAL

        rv_study_ref.setHasFixedSize(true)
        rv_study_ref.layoutManager = layoutManager
        rv_study_ref.addItemDecoration(MarginItemDecorationHorizontal(margin.toInt()))
    }

    private fun getSiswaName(): String {
        val sharedPref = Preference.initPref(requireContext(), "onSignIn")
        return sharedPref.getString("name", "name").toString()
    }

    private fun getSiswaId(): String {
        val sharedPref = Preference.initPref(requireContext(), "onSignIn")
        return sharedPref.getString("id", "id").toString()
    }

    private fun getCurrentDate(interval: Int) : String {
        val c = Calendar.getInstance()

        val year = c.get(Calendar.YEAR)
        var month = c.get(Calendar.MONTH) + 1
        var day = c.get(Calendar.DAY_OF_MONTH) - interval

        val stringDay: String
        val stringMonth: String

        if (day < 1) {
            day = 27
            month -= 1
        }

        stringMonth = if (month < 10) {
            "0$month"
        } else month.toString()

        stringDay = if (day < 10) {
            "0$day"
        } else day.toString()

        return "$year-$stringMonth-$stringDay"
//        + "T00:00:00Z"
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
                    tv_covid_positive.isInvisible = state
                    tv_covid_dead.isInvisible = state
                    tv_covid_health.isInvisible = state
                }
            }
            "referensi" -> {
                if (pb_referensi != null && rv_study_ref != null) {
                    pb_referensi.isVisible = state
                    rv_study_ref.isInvisible = state
                }
            }
        }
    }
}
