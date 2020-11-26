package com.sekon.app

import android.app.Application
import com.cloudinary.android.MediaManager
import com.sekon.app.utils.Cloudinary

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        val config: MutableMap<String, String> = HashMap()

        config["cloud_name"] = Cloudinary.CLOUD_NAME
        config["api_key"] = Cloudinary.API_KEY
        config["api_secret"] = Cloudinary.API_SECRET
        MediaManager.init(this, config)
    }
}