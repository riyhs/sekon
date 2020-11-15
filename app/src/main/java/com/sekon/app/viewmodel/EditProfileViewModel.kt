package com.sekon.app.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sekon.app.model.Resource
import com.sekon.app.model.SiswaResponse
import com.sekon.app.network.NetworkConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditProfileViewModel: ViewModel() {
    private val profileUrl = MutableLiveData<Resource<String>>()

    private val vmJob = Job()
    private val scope = CoroutineScope(Dispatchers.Default + vmJob)

    fun setProfileUrl(id: String) = scope.launch {
        profileUrl.postValue(Resource.Loading())

        NetworkConfig()
            .getService()
            .getSiswa(id)
            .enqueue(object : Callback<SiswaResponse> {
                override fun onResponse(
                    call: Call<SiswaResponse>,
                    response: Response<SiswaResponse>
                ) {
                    val url = response.body()?.result?.photo
                    profileUrl.postValue(Resource.Success(url))
                }

                override fun onFailure(call: Call<SiswaResponse>, t: Throwable) {
                    profileUrl.postValue(Resource.Error(t.message))
                }

            })
    }

    fun getProfileUrl(): MutableLiveData<Resource<String>> {
        return profileUrl
    }
}