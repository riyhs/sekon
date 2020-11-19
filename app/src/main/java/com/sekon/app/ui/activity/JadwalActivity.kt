package com.sekon.app.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.sekon.app.R
import com.sekon.app.model.Resource
import com.sekon.app.viewmodel.JadwalViewModel

class JadwalActivity : AppCompatActivity() {

    private lateinit var jadwalViewModel: JadwalViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_jadwal)

        jadwalViewModel = ViewModelProvider(this).get(JadwalViewModel::class.java)
        setupJadwal()
    }

    private fun setupJadwal() {
        jadwalViewModel.setJadwal("XI-RPL-1", "senin")
        jadwalViewModel.getJadwal().observe(this, {
            when(it) {
                is Resource.Loading -> {
//                    showLoading(true)
                }

                is Resource.Success -> {
//                    showLoading(true)
                }

                is Resource.Error -> {
//                    showLoading(true)
                }
            }
        })
    }
}