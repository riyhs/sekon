package com.sekon.app.ui.activity.dashboard

import android.os.Bundle
import android.util.TypedValue
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.sekon.app.R
import com.sekon.app.adapter.SaranAdapter
import com.sekon.app.adapter.decoration.MarginItemDecorationVertical
import com.sekon.app.model.Resource
import com.sekon.app.viewmodel.DashboardSaranViewModel
import kotlinx.android.synthetic.main.activity_dashboard_saran.*

class DashboardSaranActivity : AppCompatActivity() {

    private lateinit var dashboardSaranViewModel: DashboardSaranViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard_saran)

        setupAdapter()

        dashboardSaranViewModel = ViewModelProvider(this).get(DashboardSaranViewModel::class.java)
        setupViewModel()
    }

    private fun setupViewModel() {
        dashboardSaranViewModel.setSaran()
        dashboardSaranViewModel.getSaran().observe(this, {
            when(it) {
                is Resource.Success -> {
                    if (it.data != null) {
                        rv_saran.adapter = SaranAdapter(it.data.result)
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

    private fun setupAdapter() {
        val margin = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16f, resources.displayMetrics)
        val layoutManager = LinearLayoutManager(this)

        layoutManager.stackFromEnd = true
        layoutManager.reverseLayout = true

        rv_saran.setHasFixedSize(true)
        rv_saran.layoutManager = layoutManager
        rv_saran.addItemDecoration(MarginItemDecorationVertical(margin.toInt(), true))
    }
}