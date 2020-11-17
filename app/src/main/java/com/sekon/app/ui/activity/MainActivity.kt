package com.sekon.app.ui.activity

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.sekon.app.R
import com.sekon.app.ui.fragment.main.AnnouncementFragment
import com.sekon.app.ui.fragment.main.HomeFragment
import com.sekon.app.ui.fragment.main.MoreFragment
import com.sekon.app.utils.Preference
import com.sekon.app.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        val id = initId(applicationContext)
        if (id != null) {
            setupMainViewModel(id)
        }
        setupFragment(HomeFragment())
        bottomNavClick()
    }

    private fun initId(context: Context): String? {
        val sharedPref = Preference.initPref(context, "onSignIn")
        return sharedPref.getString("id", "id")
    }

    private fun setupMainViewModel(id: String) {
        mainViewModel.setSiswaDetail(id)
    }

    private fun bottomNavClick() {
        bottomNavigationView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.bottom_nav_home -> {
                    setupFragment(HomeFragment())
                    true
                }

                R.id.bottom_nav_announcement -> {
                    setupFragment(AnnouncementFragment())
                    true
                }

                R.id.bottom_nav_more -> {
                    setupFragment(MoreFragment())
                    true
                }

                else -> false
            }
        }
    }

    private fun setupFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.frame_container, fragment, fragment::class.java.simpleName)
            .commit()
    }
}