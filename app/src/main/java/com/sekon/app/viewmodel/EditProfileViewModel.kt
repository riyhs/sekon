package com.sekon.app.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.ErrorInfo
import com.cloudinary.android.callback.UploadCallback
import com.sekon.app.model.Resource
import com.sekon.app.model.SiswaResponse
import com.sekon.app.model.SiswaResponseDetail
import com.sekon.app.model.SiswaUpdateBody
import com.sekon.app.network.NetworkConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditProfileViewModel: ViewModel() {
    private val siswa = MutableLiveData<Resource<SiswaResponseDetail>>()
    private var postUrl = MutableLiveData<Resource<String>>()

    private val vmJob = Job()
    private val scope = CoroutineScope(Dispatchers.Default + vmJob)

    fun setProfileUrl(id: String) = scope.launch {
        siswa.postValue(Resource.Loading())

        NetworkConfig()
            .getService()
            .getSiswa(id)
            .enqueue(object : Callback<SiswaResponse> {
                override fun onResponse(
                    call: Call<SiswaResponse>,
                    response: Response<SiswaResponse>
                ) {
                    siswa.postValue(Resource.Success(response.body()?.result))
                }

                override fun onFailure(call: Call<SiswaResponse>, t: Throwable) {
                    siswa.postValue(Resource.Error(t.message))
                }

            })
    }

    fun getSiswa(): MutableLiveData<Resource<SiswaResponseDetail>> {
        return siswa
    }

    // cloudinary
    fun uploadToCloudinary(filePath: String?) {
        MediaManager.get().upload(filePath).callback(object : UploadCallback {
            override fun onStart(requestId: String) {
                Log.d("PHOTO_URL", "starting")
                postUrl.postValue(Resource.Loading())
            }

            override fun onProgress(requestId: String, bytes: Long, totalBytes: Long) {
                postUrl.postValue(Resource.Loading())
            }

            override fun onSuccess(requestId: String, resultData: Map<*, *>) {
                postUrl.postValue(Resource.Success(resultData["secure_url"].toString()))
                Log.d("PHOTO_URL", resultData["secure_url"].toString())
            }

            override fun onError(requestId: String, error: ErrorInfo) {
                Log.d("PHOTO_URL", "error ${error.description}")
                postUrl.postValue(Resource.Error(error.description))
            }

            override fun onReschedule(requestId: String, error: ErrorInfo) {
                Log.d("PHOTO_URL", "reschecule")
            }
        }).dispatch()
    }

    fun getNewUrl(): MutableLiveData<Resource<String>> {
        return postUrl
    }

    // update
    fun setUpdatePhoto(idSiswa: String, siswaUpdateBody: SiswaUpdateBody) {
        siswa.postValue(Resource.Loading())
        scope.launch {
            NetworkConfig()
                .getService()
                .updateSiswa(idSiswa, siswaUpdateBody)
                .enqueue(object : Callback<SiswaResponse> {
                    override fun onResponse(
                        call: Call<SiswaResponse>,
                        response: Response<SiswaResponse>
                    ) {
                        Log.d("PHOTO_URL", response.body()?.result?.photo.toString())
                        siswa.postValue(Resource.Success(response.body()?.result))
                    }

                    override fun onFailure(call: Call<SiswaResponse>, t: Throwable) {
                        siswa.postValue(Resource.Error(t.message))
                    }
                })
        }
    }

    override fun onCleared() {
        super.onCleared()
        vmJob.cancel()
    }
}