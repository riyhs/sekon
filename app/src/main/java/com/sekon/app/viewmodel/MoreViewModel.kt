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
    private var url = MutableLiveData<String>()

    private var vmJob = Job()
    private var scope = CoroutineScope(Dispatchers.Default + vmJob)

    fun setUpdatePhoto(idSiswa: String, siswaUpdateBody: SiswaUpdateBody, context: Context, imageView: ImageView) {
        scope.launch {
            NetworkConfig()
                .getService()
                .updateSiswa(idSiswa, siswaUpdateBody)
                .enqueue(object : Callback<SiswaResponse> {
                    override fun onResponse(
                        call: Call<SiswaResponse>,
                        response: Response<SiswaResponse>
                    ) {
                        setupGlide(context, response.body()!!.result.photo, imageView)
                    }

                    override fun onFailure(call: Call<SiswaResponse>, t: Throwable) {
                        Log.d("PHOTO_URL", "Gagal get SISWA photo URL${t.message}")
                    }
                })
        }
    }

    fun setupGlide(context: Context, photo_url: String, imageView: ImageView) {
        Glide.with(context)
            .load(photo_url)
            .centerCrop()
            .into(imageView)
    }

    fun uploadToCloudinary(filePath: String?) {
        MediaManager.get().upload(filePath).callback(object : UploadCallback {
            override fun onStart(requestId: String) {
                Log.d("PHOTO_URL", "starting")
            }

            override fun onProgress(requestId: String, bytes: Long, totalBytes: Long) {
                Log.d("PHOTO_URL", "progresss")
            }

            override fun onSuccess(requestId: String, resultData: Map<*, *>) {
                url.postValue(resultData["secure_url"].toString())
                Log.d("PHOTO_URL", url.toString())
            }

            override fun onError(requestId: String, error: ErrorInfo) {
                Log.d("PHOTO_URL", "error ${error.description}")
            }

            override fun onReschedule(requestId: String, error: ErrorInfo) {
                Log.d("PHOTO_URL", "reschecule")
            }
        }).dispatch()
    }

    fun getSiswaUrl(): MutableLiveData<String> {
        return url
    }

    override fun onCleared() {
        super.onCleared()
        vmJob.cancel()
    }
}