package com.riyaldi.sekonforteacher

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btOnClick()

    }

    private fun btOnClick() {
        bt_saran.setOnClickListener {
            val intent = Intent(this, SaranActivity::class.java)
            startActivity(intent)
        }

        bt_pengumuman.setOnClickListener {
            val intent = Intent(this, PengumumanActivity::class.java)
            startActivity(intent)
        }
    }
}