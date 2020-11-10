package com.sekon.sekonforteacher

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.sekon.sekonforteacher.model.Resource
import com.sekon.sekonforteacher.model.pengumuman.PostPengumuman
import com.sekon.sekonforteacher.viewmodel.DashboardPengumumanViewModel
import kotlinx.android.synthetic.main.activity_dashboard_pengumuman.*

class DashboardPengumumanActivity : AppCompatActivity() {

    private lateinit var dashboardPengumumanViewModel: DashboardPengumumanViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard_pengumuman)

        dashboardPengumumanViewModel = ViewModelProvider(this).get(DashboardPengumumanViewModel::class.java)

        bt_post_pengumuman.setOnClickListener {
            showLoading(true)

            val judul = et_judul_pengumuman.text
            val deskripsi = et_deskripsi_pengumuman.text

            if (judul != null && deskripsi != null) {
                if (judul.isNotEmpty() && deskripsi.isNotEmpty()) {
                    setupViewModel(initData())
                }
            } else {
                Toast.makeText(this, "Data yang dimasukan harus lengkap!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun initData(): PostPengumuman {
        val judul = et_judul_pengumuman.text
        val deskripsi = et_deskripsi_pengumuman.text

        return PostPengumuman(judul = judul.toString(), deskripsi = deskripsi.toString(), photo = "")
    }

    private fun setupViewModel(pengumuman: PostPengumuman) {
        dashboardPengumumanViewModel.setPengumuman(pengumuman)
        dashboardPengumumanViewModel.getPengumuman().observe(this, {
            when(it) {
                is Resource.Success -> {
                    if (it.data != null) {
                        Toast.makeText(this, "Sukses Menambah Pengumuman", Toast.LENGTH_SHORT).show()
                        showLoading(false)
                        resetInputText()
                    }
                }

                is Resource.Loading -> {
                    showLoading(true)
                }

                is Resource.Error -> {
                    Toast.makeText(this, "Gagal : ${it.message}", Toast.LENGTH_SHORT).show()
                    showLoading(false)
                }
            }
        })
    }

    private fun resetInputText() {
        et_judul_pengumuman.setText("")
        et_deskripsi_pengumuman.setText("")
    }

    private fun showLoading(state: Boolean) {
        pb_pengumuman.isVisible = state
        ti_deskripsi_pengumuman.isInvisible = state
        ti_judul_pengumuman.isInvisible = state
        bt_post_pengumuman.isInvisible = state
    }
}