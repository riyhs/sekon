package com.sekon.app.ui.activity

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
import com.sekon.app.viewmodel.JadwalViewModel
import kotlinx.android.synthetic.main.activity_jadwal.*

class JadwalActivity : AppCompatActivity() {

    private lateinit var jadwalViewModel: JadwalViewModel
    private lateinit var selectedChip: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_jadwal)

        showLoading(true)
        jadwalViewModel = ViewModelProvider(this).get(JadwalViewModel::class.java)

        setupAdapter()

        selectedChip = "senin"
        setupJadwal("XI-RPL-1", selectedChip)

        chipOnClickListener()
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
                        tv_jadwal_hari.text = hari.toUpperCase()
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

    private fun chipOnClickListener() {
        chip_senin.setOnClickListener {
            selectedChip = "senin"
            setupJadwal("XI-RPL-1", selectedChip)
        }
        chip_selasa.setOnClickListener {
            selectedChip = "selasa"
            setupJadwal("XI-RPL-1", selectedChip)
        }
        chip_rabu.setOnClickListener {
            selectedChip = "rabu"
            setupJadwal("XI-RPL-1", selectedChip)
        }
        chip_kamis.setOnClickListener {
            selectedChip = "kamis"
            setupJadwal("XI-RPL-1", selectedChip)
        }
        chip_jumat.setOnClickListener {
            selectedChip = "jumat"
            setupJadwal("XI-RPL-1", selectedChip)
        }
    }

    private fun showLoading(state: Boolean) {
        pb_jadwal.isVisible = state
        nsv_jadwal.isInvisible = state
    }
}