package com.sekon.app.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sekon.app.model.Resource
import com.sekon.app.model.jadwal.JadwalResponse
import com.sekon.app.network.NetworkConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class JadwalViewModel: ViewModel() {
    private val job = Job()
    private val scope = CoroutineScope(Dispatchers.Default + job)

    private val jadwalResponse = MutableLiveData<Resource<JadwalResponse>>()

    fun setJadwal(kelas: String, hari: String) = scope.launch {

        jadwalResponse.postValue(Resource.Loading())

        NetworkConfig()
            .getService()
            .getJadwal(kelas, hari)
            .enqueue(object : Callback<JadwalResponse> {
                override fun onResponse(
                    call: Call<JadwalResponse>,
                    response: Response<JadwalResponse>
                ) {
                    jadwalResponse.postValue(Resource.Success(response.body()))
                }

                override fun onFailure(call: Call<JadwalResponse>, t: Throwable) {
                    jadwalResponse.postValue(Resource.Error(t.message))
                }

            })
    }

    fun getJadwal(): MutableLiveData<Resource<JadwalResponse>> {
        return jadwalResponse
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}