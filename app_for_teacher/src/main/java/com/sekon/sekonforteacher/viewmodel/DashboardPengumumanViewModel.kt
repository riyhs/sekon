package com.sekon.sekonforteacher.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sekon.sekonforteacher.model.Resource
import com.sekon.sekonforteacher.model.pengumuman.PostPengumuman
import com.sekon.sekonforteacher.model.pengumuman.PostPengumumanResponse
import com.sekon.sekonforteacher.network.NetworkConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DashboardPengumumanViewModel: ViewModel() {
    private val pengumumanResponse = MutableLiveData<Resource<PostPengumumanResponse>>()

    private var vmJob = Job()
    private var scope = CoroutineScope(Dispatchers.Default + vmJob)

    fun setPengumuman(pengumuman : PostPengumuman) {
        scope.launch {
            pengumumanResponse.postValue(Resource.Loading())

            NetworkConfig()
                .getService()
                .postPengumuman(pengumuman)
                .enqueue(object : Callback<PostPengumumanResponse>{
                    override fun onResponse(
                        call: Call<PostPengumumanResponse>,
                        response: Response<PostPengumumanResponse>
                    ) {
                        pengumumanResponse.postValue(Resource.Success(response.body()))
                    }

                    override fun onFailure(call: Call<PostPengumumanResponse>, t: Throwable) {
                        pengumumanResponse.postValue(Resource.Error(t.message))
                    }
                })

        }
    }

    fun getPengumuman(): MutableLiveData<Resource<PostPengumumanResponse>> {
        return pengumumanResponse
    }

    override fun onCleared() {
        super.onCleared()
        vmJob.cancel()
    }
}