package com.sekon.app.ui.activity.dashboard

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.sekon.app.R
import com.sekon.app.ui.activity.SplashActivity
import com.sekon.app.utils.Preference
import kotlinx.android.synthetic.main.activity_dashboard.*

class DashboardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        btOnClick()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.action_bar_nav_profile, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_bar_log_out -> {
                Toast.makeText(this, "Log Out", Toast.LENGTH_SHORT).show()
                Preference.logOut(this)
                startActivity(
                    Intent(this, SplashActivity::class.java)
                )
                finish()
            }

            R.id.action_edit_profile -> {
                Toast.makeText(this, "Edit Profile", Toast.LENGTH_SHORT).show()
            }
        }

        return true
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

        bt_referensi.setOnClickListener {
            val intent = Intent(this, DashboardReferenceActivity::class.java)
            startActivity(intent)
        }
    }
}