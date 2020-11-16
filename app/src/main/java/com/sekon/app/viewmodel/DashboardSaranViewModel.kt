package com.sekon.app.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sekon.app.model.Resource
import com.sekon.app.model.saran.SaranResponse
import com.sekon.app.network.NetworkConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DashboardSaranViewModel: ViewModel() {
    private val saranResponse = MutableLiveData<Resource<SaranResponse>>()

    private var vmJob = Job()
    private var scope = CoroutineScope(Dispatchers.Default + vmJob)

    fun setSaran() {
        scope.launch {
            saranResponse.postValue(Resource.Loading())

            NetworkConfig()
                .getService()
                .getSaran()
                .enqueue(object : Callback<SaranResponse>{
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

    fun getSaran(): MutableLiveData<Resource<SaranResponse>> {
        return saranResponse
    }

    override fun onCleared() {
        super.onCleared()
        vmJob.cancel()
    }
}