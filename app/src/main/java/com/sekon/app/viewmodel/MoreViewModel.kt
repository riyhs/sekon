package com.sekon.app.viewmodel

import android.content.Context
import android.util.Log
import android.widget.ImageView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bumptech.glide.Glide
import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.ErrorInfo
import com.cloudinary.android.callback.UploadCallback
import com.sekon.app.model.SiswaResponse
import com.sekon.app.model.SiswaUpdateBody
import com.sekon.app.network.NetworkConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MoreViewModel : ViewModel() {

    private var vmJob = Job()
    private var scope = CoroutineScope(Dispatchers.Default + vmJob)

    fun setupGlide(context: Context, photo_url: String, imageView: ImageView) {
        Glide.with(context)
            .load(photo_url)
            .centerCrop()
            .into(imageView)
    }

    override fun onCleared() {
        super.onCleared()
        vmJob.cancel()
    }
}