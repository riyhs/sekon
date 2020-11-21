package com.sekon.app.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.ErrorInfo
import com.cloudinary.android.callback.UploadCallback
import com.sekon.app.model.Resource
import com.sekon.app.model.announcement.AnnouncementPostModel
import com.sekon.app.model.announcement.AnnouncementPostResponse
import com.sekon.app.network.NetworkConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DashboardAnnouncementViewModel: ViewModel() {
    private val pengumumanResponse = MutableLiveData<Resource<AnnouncementPostResponse>>()
    private val photoUrl = MutableLiveData<Resource<String>>()

    private var vmJob = Job()
    private var scope = CoroutineScope(Dispatchers.Default + vmJob)

    fun setPengumuman(announcement : AnnouncementPostModel) {
        scope.launch {
            pengumumanResponse.postValue(Resource.Loading())

            NetworkConfig()
                .getService()
                .postPengumuman(announcement)
                .enqueue(object : Callback<AnnouncementPostResponse>{
                    override fun onResponse(
                        call: Call<AnnouncementPostResponse>,
                        response: Response<AnnouncementPostResponse>
                    ) {
                        pengumumanResponse.postValue(Resource.Success(response.body()))
                    }

                    override fun onFailure(call: Call<AnnouncementPostResponse>, t: Throwable) {
                        pengumumanResponse.postValue(Resource.Error(t.message))
                    }
                })

        }
    }

    fun getPengumuman(): MutableLiveData<Resource<AnnouncementPostResponse>> {
        return pengumumanResponse
    }

    // cloudinary
    fun uploadToCloudinary(filePath: String?) {
        MediaManager.get().upload(filePath).callback(object : UploadCallback {
            override fun onStart(requestId: String) {
                Log.d("PHOTO_URL", "starting")
                photoUrl.postValue(Resource.Loading())
            }

            override fun onProgress(requestId: String, bytes: Long, totalBytes: Long) {
                photoUrl.postValue(Resource.Loading())
            }

            override fun onSuccess(requestId: String, resultData: Map<*, *>) {
                photoUrl.postValue(Resource.Success(resultData["secure_url"].toString()))
                Log.d("PHOTO_URL", resultData["secure_url"].toString())
            }

            override fun onError(requestId: String, error: ErrorInfo) {
                Log.d("PHOTO_URL", "error ${error.description}")
                photoUrl.postValue(Resource.Error(error.description))
            }

            override fun onReschedule(requestId: String, error: ErrorInfo) {
                Log.d("PHOTO_URL", "reschecule")
            }
        }).dispatch()
    }

    fun getPhotoUrl(): MutableLiveData<Resource<String>> {
        return photoUrl
    }

    override fun onCleared() {
        super.onCleared()
        vmJob.cancel()
    }
}