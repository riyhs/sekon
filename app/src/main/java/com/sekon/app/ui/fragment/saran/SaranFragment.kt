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
import androidx.recyclerview.widget.LinearLayoutManager
import com.sekon.app.R
import com.sekon.app.adapter.SaranAdapter
import com.sekon.app.adapter.decoration.MarginItemDecorationVertical
import com.sekon.app.model.Resource
import com.sekon.app.model.saran.SaranResponseDetail
import com.sekon.app.viewmodel.SaranViewModel
import kotlinx.android.synthetic.main.fragment_saran.*

class SaranFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_saran, container, false)
    }

    private lateinit var saranViewModel: SaranViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showLoading(true)

        bt_ajukan_saran.setOnClickListener {
            setupFragment(AddSaranFragment())
        }

        saranViewModel = ViewModelProvider(this).get(SaranViewModel::class.java)
        setupViewModel()
    }

    private fun setupAdapter(listSaran: List<SaranResponseDetail>) {
        val margin = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16f, resources.displayMetrics)

        rv_saran.setHasFixedSize(true)
        rv_saran.addItemDecoration(MarginItemDecorationVertical(margin.toInt()))
        rv_saran.layoutManager = LinearLayoutManager(context)
        rv_saran.adapter = SaranAdapter(listSaran)
        rv_saran.isNestedScrollingEnabled = false
    }

    private fun setupFragment(fragment: Fragment) {
        activity?.supportFragmentManager
            ?.beginTransaction()
            ?.replace(R.id.fragment_saran, fragment, fragment::class.java.simpleName)
            ?.commit()
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
                        setupAdapter(it.data.result)
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