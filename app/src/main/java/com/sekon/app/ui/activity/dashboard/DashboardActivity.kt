package com.sekon.app.ui.activity.dashboard

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.sekon.app.R
import com.sekon.app.ui.activity.SplashActivity
import com.sekon.app.utils.Preference
import kotlinx.android.synthetic.main.activity_dashboard.*

class DashboardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        btOnClick(getKelas())
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.action_bar_nav_profile, menu)
        if (menu != null) {
            menu.findItem(R.id.action_edit_profile)?.isVisible = false
        }
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

    private fun getKelas(): String {
        val sharedPref = Preference.initPref(this, "onSignIn")
        return sharedPref.getString("kelas", "kelas").toString()
    }

    private fun btOnClick(kelas: String) {
        mcv_saran.setOnClickListener {
            val intent = Intent(this, DashboardSaranActivity::class.java)
            startActivity(intent)
        }

        mcv_pengumuman.setOnClickListener {
            val intent = Intent(this, DashboardAnnouncementActivity::class.java)
            startActivity(intent)
        }

        mcv_referensi.setOnClickListener {
            val intent = Intent(this, DashboardReferenceActivity::class.java)
            startActivity(intent)
        }

        mcv_absen.setOnClickListener {
            val url = "https://sekon.netlify.app/absen/$kelas"
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            
            startActivity(intent)
        }
    }
}