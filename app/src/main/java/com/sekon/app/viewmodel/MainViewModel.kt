package com.sekon.app.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sekon.app.model.SiswaResponse
import com.sekon.app.network.NetworkConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel: ViewModel() {
    private var siswaDetail = MutableLiveData<SiswaResponse>()

    private var vmJob = Job()
    private var scope = CoroutineScope(Dispatchers.Default + vmJob)

    fun setSiswaDetail(idSiswa: String) {
        scope.launch {
            NetworkConfig()
                .getService()
                .getSiswa(idSiswa)
                .enqueue(object : Callback<SiswaResponse> {
                    override fun onResponse(
                        call: Call<SiswaResponse>,
                        response: Response<SiswaResponse>
                    ) {
                        siswaDetail.postValue(response.body())
                    }

                    override fun onFailure(call: Call<SiswaResponse>, t: Throwable) {
                        Log.d("SISWA", "Gagal get SISWA ${t.message}")
                    }
                })
        }
    }

    fun getSiswaDetail(): MutableLiveData<SiswaResponse> {
        return siswaDetail
    }

    override fun onCleared() {
        super.onCleared()
        vmJob.cancel()
    }
}