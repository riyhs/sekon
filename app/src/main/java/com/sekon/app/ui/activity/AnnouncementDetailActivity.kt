package com.sekon.app.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.bumptech.glide.Glide
import com.sekon.app.R
import com.sekon.app.model.announcement.AnnouncementResponseDetail
import kotlinx.android.synthetic.main.activity_announcement_detail.*

class AnnouncementDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_announcement_detail)

        val announcement = intent.getParcelableExtra<AnnouncementResponseDetail>("announcement")

        if (announcement != null) {
            Glide.with(this)
                .load(announcement.photo)
                .into(iv_detail_announcement)

            tv_title_pengumuman.text = announcement.judul
            tv_detail_pengumumann.text = announcement.deskripsi
        } else {
            Toast.makeText(this, "Gagal mendapat data", Toast.LENGTH_SHORT).show()
        }
    }
}