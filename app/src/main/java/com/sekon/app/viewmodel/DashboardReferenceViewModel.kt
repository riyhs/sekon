package com.sekon.app.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
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

    override fun onCleared() {
        super.onCleared()
        vmJob.cancel()
    }
}