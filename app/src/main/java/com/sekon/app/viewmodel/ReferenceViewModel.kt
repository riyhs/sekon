package com.sekon.app.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sekon.app.model.reference.ReferenceResponse
import com.sekon.app.network.NetworkConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ReferenceViewModel: ViewModel() {
    val referenceResponse = MutableLiveData<ReferenceResponse>()

    private var vmJob = Job()
    private var scope = CoroutineScope(Dispatchers.Default + vmJob)

    fun setReference(kelas: String) {
        scope.launch {
            NetworkConfig()
                .getService()
                .getReferensi(kelas)
                .enqueue(object : Callback<ReferenceResponse>{
                    override fun onResponse(
                        call: Call<ReferenceResponse>,
                        response: Response<ReferenceResponse>
                    ) {
                        referenceResponse.postValue(response.body())
                    }

                    override fun onFailure(call: Call<ReferenceResponse>, t: Throwable) {
                        Log.d("REFERENSI", "gagal get referensi")
                    }

                })
        }
    }

    fun getReference() : MutableLiveData<ReferenceResponse> {
        return referenceResponse
    }

    override fun onCleared() {
        super.onCleared()
        vmJob.cancel()
    }
}