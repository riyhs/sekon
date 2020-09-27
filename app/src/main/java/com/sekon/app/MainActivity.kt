package com.sekon.app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.sekon.app.adapter.StudyRefAdapter
import com.sekon.app.model.StudyRef
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_home.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupFragment(HomeFragment())
        bottomNavClick()
    }

    private fun bottomNavClick() {
        bottomNavigationView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.bottom_nav_home -> {
                    setupFragment(HomeFragment())
                    true
                }

                R.id.bottom_nav_features -> {
                    setupFragment(FeatureFragment())
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