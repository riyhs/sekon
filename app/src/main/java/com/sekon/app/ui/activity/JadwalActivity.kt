package com.sekon.app.ui.activity

import android.os.Bundle
import android.util.TypedValue
import androidx.appcompat.app.AppCompatActivity
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_jadwal)

        setupAdapter()

        jadwalViewModel = ViewModelProvider(this).get(JadwalViewModel::class.java)
        setupJadwal("XI-RPL-1","senin")
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
//                    showLoading(true)
                }

                is Resource.Success -> {
                    if (it.data != null) {
                        rv_jadwal.adapter = JadwalAdapter(it.data.result)
                        tv_jadwal_hari.text = "Senin"
                    }
//                    showLoading(true)
                }

                is Resource.Error -> {
//                    showLoading(true)
                }
            }
        })
    }
}