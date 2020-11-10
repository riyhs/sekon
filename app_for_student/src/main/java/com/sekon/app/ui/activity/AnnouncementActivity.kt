package com.sekon.app.ui.activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.sekon.app.R
import com.sekon.app.adapter.AnnouncementAdapter
import com.sekon.app.model.Resource
import com.sekon.app.model.announcement.AnnouncementResponseDetail
import com.sekon.app.viewmodel.AnnouncementViewModel
import kotlinx.android.synthetic.main.activity_announcement.*

class AnnouncementActivity : AppCompatActivity() {

    private lateinit var announcementViewModel: AnnouncementViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_announcement)

        announcementViewModel = ViewModelProvider(this).get(AnnouncementViewModel::class.java)

        setupViewModel()
    }

    private fun setupViewModel() {
        announcementViewModel.setAnnouncement()
        announcementViewModel.getAnnouncement().observe(this, { response ->
            when (response) {
                is Resource.Success -> {
                    val announcements = response.data?.result

                    if (announcements != null && announcements.size > 1) {
                        setupAdapter(announcements)
                        showLoading(false)
                    } else {
                        Toast.makeText(this, "Tidak ada pengumuman", Toast.LENGTH_SHORT).show()
                        showLoading(false)
                    }
                }

                is Resource.Loading -> {
                    showLoading(true)
                }

                is Resource.Error -> {
                    val err = response.message
                    Toast.makeText(this, err.toString(), Toast.LENGTH_SHORT).show()
                    showLoading(false)
                }
            }
        })
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            pb_announcement.isVisible = state
            rv_announcment.isInvisible = state
        } else {
            pb_announcement.isVisible = state
            rv_announcment.isInvisible = state
        }
    }

    private fun setupAdapter(list: List<AnnouncementResponseDetail>?) {
        rv_announcment.setHasFixedSize(true)
        rv_announcment.layoutManager = LinearLayoutManager(this)
        rv_announcment.adapter = AnnouncementAdapter(list)
    }
}