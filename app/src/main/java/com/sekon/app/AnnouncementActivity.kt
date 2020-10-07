package com.sekon.app

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.sekon.app.adapter.AnnouncementAdapter
import com.sekon.app.model.Announcement
import kotlinx.android.synthetic.main.activity_announcement.*

class AnnouncementActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_announcement)

        val list = listOf(
            Announcement("Pengumuman 1", "Deskripsi"),
            Announcement("Pengumuman 2", "Deskripsi"),
            Announcement("Pengumuman 3", "Deskripsi")
        )

        if (list.size > 1) {
            setupAdapter(list)
        } else {
            Toast.makeText(this, "Tidak ada pengumuman", Toast.LENGTH_SHORT).show()
        }

    }

    private fun setupAdapter(list: List<Announcement>) {
        rv_announcment.setHasFixedSize(true)
        rv_announcment.layoutManager = LinearLayoutManager(this)
        rv_announcment.adapter = AnnouncementAdapter(list)
    }
}