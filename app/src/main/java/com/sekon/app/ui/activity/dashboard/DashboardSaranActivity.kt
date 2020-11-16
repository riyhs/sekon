package com.sekon.app.ui.activity.dashboard

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.sekon.app.R
import com.sekon.app.adapter.SaranAdapter
import com.sekon.app.model.Resource
import com.sekon.app.model.saran.SaranResponseDetail
import com.sekon.app.viewmodel.DashboardSaranViewModel
import kotlinx.android.synthetic.main.activity_dashboard_saran.*

class DashboardSaranActivity : AppCompatActivity() {

    private lateinit var dashboardSaranViewModel: DashboardSaranViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard_saran)

        dashboardSaranViewModel = ViewModelProvider(this).get(DashboardSaranViewModel::class.java)
        setupViewModel()

    }

    private fun setupViewModel() {
        dashboardSaranViewModel.setSaran()
        dashboardSaranViewModel.getSaran().observe(this, {
            when(it) {
                is Resource.Success -> {
                    if (it.data != null) {
                        setupAdapter(it.data.result)
                        showLoading(false)
                    }
                }

                is Resource.Loading -> {
                    showLoading(true)
                }

                is Resource.Error -> {
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                    showLoading(false)
                }
            }
        })
    }

    private fun showLoading(state: Boolean) {
        pb_saran.isVisible = state
        rv_saran.isInvisible = state
    }

    private fun setupAdapter(post: List<SaranResponseDetail>) {
        rv_saran.setHasFixedSize(true)
        rv_saran.layoutManager = LinearLayoutManager(this)
        rv_saran.adapter = SaranAdapter(post)
    }
}