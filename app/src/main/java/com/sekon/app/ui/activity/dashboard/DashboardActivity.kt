package com.sekon.app.ui.activity.dashboard

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.sekon.app.R
import kotlinx.android.synthetic.main.activity_dashboard.*

class DashboardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        btOnClick()

    }

    private fun btOnClick() {
        bt_saran.setOnClickListener {
            val intent = Intent(this, DashboardSaranActivity::class.java)
            startActivity(intent)
        }

        bt_pengumuman.setOnClickListener {
            val intent = Intent(this, DashboardAnnouncementActivity::class.java)
            startActivity(intent)
        }
    }
}