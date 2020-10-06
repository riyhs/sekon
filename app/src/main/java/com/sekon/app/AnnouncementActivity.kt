package com.sekon.app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.sekon.app.adapter.MainCardAdapter
import com.sekon.app.model.Announcement
import kotlinx.android.synthetic.main.activity_announcement.*
import kotlinx.android.synthetic.main.fragment_home.*

class AnnouncementActivity : AppCompatActivity() {

    private val list = listOf<Announcement>(
        Announcement("Pengumuman 1", "Deskripsi pengumuman wkwk hoi hoi saya males nulisnya"),
        Announcement("Pengumuman 2", "Deskripsi pengumuman wkwk hoi hoi saya males nulisnya"),
        Announcement("Pengumuman 3", "Deskripsi pengumuman wkwk hoi hoi saya males nulisnya")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_announcement)

        setupAdapter()

    }

    private fun setupAdapter() {
        rv_announcment.setHasFixedSize(true)
        rv_announcment.layoutManager = LinearLayoutManager(this)
//        rv_announcment.adapter = MainCardAdapter(list)
    }
}