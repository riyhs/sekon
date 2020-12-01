package com.sekon.app.ui.activity

import android.content.Context
import android.os.Bundle
import android.util.TypedValue
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.sekon.app.R
import com.sekon.app.adapter.JadwalAdapter
import com.sekon.app.adapter.decoration.MarginItemDecorationVertical
import com.sekon.app.model.Resource
import com.sekon.app.utils.Preference
import com.sekon.app.viewmodel.JadwalViewModel
import kotlinx.android.synthetic.main.activity_jadwal.*
import java.util.*

class JadwalActivity : AppCompatActivity() {

    private lateinit var jadwalViewModel: JadwalViewModel
    private lateinit var selectedChip: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_jadwal)

        showLoading(true)
        jadwalViewModel = ViewModelProvider(this).get(JadwalViewModel::class.java)

        setupAdapter()

        selectedChip = getDay()
        val kelas = getData("kelas", this)

        if (selectedChip != "libur") {
            setupJadwal(kelas, selectedChip)
        } else {
            setupJadwal(kelas, "senin")
        }

        chipOnClickListener(kelas)
    }

    private fun getDay(): String {
        val c = Calendar.getInstance()

        when (c.get(Calendar.DAY_OF_WEEK)) {
            2 -> {
                chip_senin.isChecked = true
                return "senin"
            }
            3 -> {
                chip_selasa.isChecked = true
                return "selasa"
            }
            4 -> {
                chip_rabu.isChecked = true
                return "rabu"
            }
            5 -> {
                chip_kamis.isChecked = true
                return "kamis"
            }
            6 -> {
                chip_jumat.isChecked = true
                return "jumat"
            } else -> {
                chip_senin.isChecked = false
                return "libur"
            }
        }
    }

    private fun setupAdapter() {
        val margin = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8f, resources.displayMetrics)
        val layoutManager = LinearLayoutManager(this)

        rv_jadwal.setHasFixedSize(true)
        rv_jadwal.addItemDecoration(MarginItemDecorationVertical(margin.toInt(), false))
        rv_jadwal.layoutManager = layoutManager
        rv_jadwal.isNestedScrollingEnabled = false
    }

    private fun setupJadwal(kelas: String, hari: String) {
        jadwalViewModel.setJadwal(kelas, hari)
        jadwalViewModel.getJadwal().observe(this, {
            when(it) {
                is Resource.Loading -> {
                    showLoading(true)
                }

                is Resource.Success -> {
                    if (it.data != null) {
                        rv_jadwal.adapter = JadwalAdapter(it.data.result)
                        tv_jadwal_hari.text = hari.toUpperCase(Locale.ROOT)
                    }
                    showLoading(false)
                }

                is Resource.Error -> {
                    Toast.makeText(this, it.message.toString(), Toast.LENGTH_LONG).show()
                    showLoading(false)
                }
            }
        })
    }

    private fun getData(name: String, context: Context): String {
        val sharedPref = Preference.initPref(context, "onSignIn")
        return sharedPref.getString(name, name).toString()
    }

    private fun chipOnClickListener(kelas: String) {
        chip_senin.setOnClickListener {
            selectedChip = "senin"
            setupJadwal(kelas, selectedChip)
        }
        chip_selasa.setOnClickListener {
            selectedChip = "selasa"
            setupJadwal(kelas, selectedChip)
        }
        chip_rabu.setOnClickListener {
            selectedChip = "rabu"
            setupJadwal(kelas, selectedChip)
        }
        chip_kamis.setOnClickListener {
            selectedChip = "kamis"
            setupJadwal(kelas, selectedChip)
        }
        chip_jumat.setOnClickListener {
            selectedChip = "jumat"
            setupJadwal(kelas, selectedChip)
        }
    }

    private fun showLoading(state: Boolean) {
        pb_jadwal.isVisible = state
        nsv_jadwal.isInvisible = state
    }
}