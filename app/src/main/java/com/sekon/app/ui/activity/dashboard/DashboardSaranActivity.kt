package com.sekon.app.ui.activity.dashboard

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sekon.app.R

class DashboardSaranActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard_saran)

        supportActionBar?.title = "Saran"
        actionBar?.title = "Saran"

    }
}