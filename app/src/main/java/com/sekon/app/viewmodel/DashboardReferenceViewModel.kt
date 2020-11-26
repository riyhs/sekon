package com.sekon.app.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.ErrorInfo
import com.cloudinary.android.callback.UploadCallback
import com.sekon.app.model.Resource
import com.sekon.app.model.reference.PostReferenceBody
import com.sekon.app.model.reference.PostReferenceResponse
import com.sekon.app.network.NetworkConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DashboardReferenceViewModel: ViewModel() {
    private var vmJob = Job()
    private var scope = CoroutineScope(Dispatchers.Default + vmJob)

    private var referenceResponse = MutableLiveData<Resource<PostReferenceResponse>>()
    private val photoUrl = MutableLiveData<Resource<String>>()

    fun postReference(reference : PostReferenceBody) {
        referenceResponse.postValue(Resource.Loading())

        scope.launch {
            NetworkConfig()
                .getService()
                .postReferensi(reference)
                .enqueue(object : Callback<PostReferenceResponse> {
                    override fun onResponse(
                        call: Call<PostReferenceResponse>,
                        response: Response<PostReferenceResponse>
                    ) {
                        referenceResponse.postValue(Resource.Success(response.body()))
                    }

                    override fun onFailure(call: Call<PostReferenceResponse>, t: Throwable) {
                        referenceResponse.postValue(Resource.Error(t.message))
                    }

                })
        }
    }

    fun getReference(): MutableLiveData<Resource<PostReferenceResponse>> {
        return referenceResponse
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