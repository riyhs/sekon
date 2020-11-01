package com.sekon.app

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import com.cloudinary.android.MediaManager
import com.sekon.app.utils.Cloudinary

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val config: MutableMap<String, String> = HashMap()

        config["cloud_name"] = Cloudinary.CLOUD_NAME
        config["api_key"] = Cloudinary.API_KEY
        config["api_secret"] = Cloudinary.API_SECRET
        MediaManager.init(this, config)

        supportActionBar?.hide()
    }
}