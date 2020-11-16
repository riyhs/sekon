package com.sekon.app.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.ErrorInfo
import com.cloudinary.android.callback.UploadCallback
import com.sekon.app.model.Resource
import com.sekon.app.model.saran.PostSaran
import com.sekon.app.model.saran.PostSaranResponse
import com.sekon.app.model.saran.SaranResponse
import com.sekon.app.network.NetworkConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SaranViewModel: ViewModel() {
    private var imageUrl = MutableLiveData<String>()
    private var saranResponse = MutableLiveData<Resource<SaranResponse>>()
    private var postSaranResponse = MutableLiveData<Resource<PostSaranResponse>>()

    private var vmJob = Job()
    private var scope = CoroutineScope(Dispatchers.Default + vmJob)

    // get data == call get api -> getResponse -> send to adapter

    fun setSaran() {
        scope.launch {
            saranResponse.postValue(Resource.Loading())

            NetworkConfig()
                .getService()
                .getSaran()
                .enqueue(object : Callback<SaranResponse> {
                    override fun onResponse(
                        call: Call<SaranResponse>,
                        response: Response<SaranResponse>
                    ) {
                        saranResponse.postValue(Resource.Success(response.body()))
                    }

                    override fun onFailure(call: Call<SaranResponse>, t: Throwable) {
                        saranResponse.postValue(Resource.Error(t.message))
                    }

                })
        }
    }

    fun getSaranResponse(): MutableLiveData<Resource<SaranResponse>> {
        return saranResponse
    }


    // upload new data == uploadimage to cloudinary -> get url -> call post api -> send body(url, dll) -> get response -> send to adapter

    fun uploadToCloudinary(filePath: String?) {
        MediaManager.get().upload(filePath).callback(object : UploadCallback {
            override fun onStart(requestId: String) {
            }

            override fun onProgress(requestId: String, bytes: Long, totalBytes: Long) {
            }

            override fun onSuccess(requestId: String, resultData: Map<*, *>) {
                imageUrl.postValue(resultData["secure_url"].toString())
                Log.d("URL", imageUrl.toString())
            }

            override fun onError(requestId: String, error: ErrorInfo) {
            }

            override fun onReschedule(requestId: String, error: ErrorInfo) {
            }
        }).dispatch()
    }

    fun getImageUrl(): MutableLiveData<String> {
        return imageUrl
    }

    fun postSaran(saran: PostSaran) {
        scope.launch {

            postSaranResponse.postValue(Resource.Loading())

            NetworkConfig()
                .getService()
                .postSaran(saran)
                .enqueue(object : Callback<PostSaranResponse> {
                    override fun onResponse(
                        call: Call<PostSaranResponse>,
                        response: Response<PostSaranResponse>
                    ) {
                        postSaranResponse.postValue(Resource.Success(response.body()))
                    }

                    override fun onFailure(call: Call<PostSaranResponse>, t: Throwable) {
                        postSaranResponse.postValue(Resource.Error(t.message))
                    }

                })
        }
    }

    fun getPostSaranResponse(): MutableLiveData<Resource<PostSaranResponse>> {
        return postSaranResponse
    }

    override fun onCleared() {
        super.onCleared()
        vmJob.cancel()
    }
}