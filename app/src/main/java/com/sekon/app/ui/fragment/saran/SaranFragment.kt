package com.sekon.app.ui.fragment.saran

import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.sekon.app.R
import com.sekon.app.adapter.SaranAdapter
import com.sekon.app.adapter.decoration.MarginItemDecorationVertical
import com.sekon.app.model.Resource
import com.sekon.app.viewmodel.SaranViewModel
import kotlinx.android.synthetic.main.fragment_saran.*

class SaranFragment : Fragment() {

    private lateinit var saranViewModel: SaranViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_saran, container, false)

        saranViewModel = ViewModelProvider(requireActivity()).get(SaranViewModel::class.java)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showLoading(true)

        bt_ajukan_saran.setOnClickListener {
            findNavController().navigate(R.id.action_saranFragment_to_addSaranFragment)
        }

        setupAdapter()
        setupViewModel()
    }

    private fun setupAdapter() {
        val margin = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16f, resources.displayMetrics)
        val layoutManager = LinearLayoutManager(context)

        layoutManager.stackFromEnd = true
        layoutManager.reverseLayout = true

        rv_saran.setHasFixedSize(false)
        rv_saran.addItemDecoration(MarginItemDecorationVertical(margin.toInt(), true))
        rv_saran.layoutManager = layoutManager
        rv_saran.isNestedScrollingEnabled = false
    }

    private fun setupViewModel() {
        saranViewModel.setSaran()
        saranViewModel.getSaranResponse().observe(viewLifecycleOwner, {
            when (it) {
                is Resource.Loading -> {
                    showLoading(true)
                }

                is Resource.Error -> {
                    showLoading(false)
                    Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                }

                is Resource.Success -> {
                    if (it.data != null) {
                        rv_saran.adapter = SaranAdapter(it.data.result)
                    }
                    showLoading(false)
                }
            }
        })
    }

    private fun showLoading(state: Boolean) {
        pb_saran_main.isVisible = state
        rv_saran.isInvisible = state
        bt_ajukan_saran.isInvisible = state
    }
}