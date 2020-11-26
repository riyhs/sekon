package com.sekon.app.ui.fragment.dashboard

import android.os.Bundle
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.sekon.app.R
import com.sekon.app.adapter.SaranAdapter
import com.sekon.app.adapter.decoration.MarginItemDecorationVertical
import com.sekon.app.model.Resource
import com.sekon.app.viewmodel.DashboardSaranViewModel
import kotlinx.android.synthetic.main.fragment_dashboard_saran.*

class DashboardSaranFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_dashboard_saran, container, false)
    }

    private lateinit var dashboardSaranViewModel: DashboardSaranViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupAdapter()

        dashboardSaranViewModel = ViewModelProvider(this).get(DashboardSaranViewModel::class.java)
        setupViewModel()
    }

    private fun setupViewModel() {
        dashboardSaranViewModel.setSaran()
        dashboardSaranViewModel.getSaran().observe(viewLifecycleOwner, {
            when(it) {
                is Resource.Success -> {
                    if (it.data != null) {
                        rv_saran.adapter = SaranAdapter(it.data.result, true)
                        showLoading(false)
                    }
                }

                is Resource.Loading -> {
                    showLoading(true)
                }

                is Resource.Error -> {
                    Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
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
        val layoutManager = LinearLayoutManager(context)

        layoutManager.stackFromEnd = true
        layoutManager.reverseLayout = true

        rv_saran.setHasFixedSize(true)
        rv_saran.layoutManager = layoutManager
        rv_saran.addItemDecoration(MarginItemDecorationVertical(margin.toInt(), true))
    }
}