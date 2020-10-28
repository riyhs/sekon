package com.sekon.app

import android.animation.ObjectAnimator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val startColor = window.statusBarColor
        val endColor = ContextCompat.getColor(this, R.color.colorAccent)
        ObjectAnimator.ofArgb(window, "statusBarColor", startColor, endColor).start()

        supportActionBar?.hide()
    }
}