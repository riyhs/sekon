package com.riyaldi.sekonforteacher

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.riyaldi.sekonforteacher.adapter.SaranAdapter
import com.riyaldi.sekonforteacher.model.Resource
import com.riyaldi.sekonforteacher.model.saran.Post
import com.riyaldi.sekonforteacher.viewmodel.SaranViewModel
import kotlinx.android.synthetic.main.activity_saran.*

class SaranActivity : AppCompatActivity() {

    private lateinit var saranViewModel: SaranViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_saran)

        saranViewModel = ViewModelProvider(this).get(SaranViewModel::class.java)
        setupViewModel()

    }

    private fun setupViewModel() {
        saranViewModel.setSaran()
        saranViewModel.getSaran().observe(this, {
            when(it) {
                is Resource.Success -> {
                    if (it.data != null) {
                        setupAdapter(it.data.post)
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

    private fun setupAdapter(post: List<Post>) {
        rv_saran.setHasFixedSize(true)
        rv_saran.layoutManager = LinearLayoutManager(this)
        rv_saran.adapter = SaranAdapter(post)
    }
}