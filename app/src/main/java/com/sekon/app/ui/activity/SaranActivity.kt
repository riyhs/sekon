package com.sekon.app.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.sekon.app.R
import com.sekon.app.ui.fragment.saran.SaranFragment

class SaranActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_saran)

        setupFragment(SaranFragment())
    }

    private fun setupFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_saran, fragment, fragment::class.java.simpleName)
            .commit()
    }
}