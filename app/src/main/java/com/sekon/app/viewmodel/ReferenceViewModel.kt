package com.sekon.app.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sekon.app.model.Resource
import com.sekon.app.model.reference.ReferenceResponse
import com.sekon.app.model.reference.ReferenceResponseItem
import com.sekon.app.network.NetworkConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ReferenceViewModel: ViewModel() {
    val referenceResponse = MutableLiveData<Resource<List<ReferenceResponseItem>>>()

    private var vmJob = Job()
    private var scope = CoroutineScope(Dispatchers.Default + vmJob)

    fun setReference(kelas: String) {
        scope.launch {
            referenceResponse.postValue(Resource.Loading())

            NetworkConfig()
                .getService()
                .getReferensi(kelas)
                .enqueue(object : Callback<ReferenceResponse>{
                    override fun onResponse(
                        call: Call<ReferenceResponse>,
                        response: Response<ReferenceResponse>
                    ) {
                        referenceResponse.postValue(Resource.Success(response.body()?.result))
                    }

                    override fun onFailure(call: Call<ReferenceResponse>, t: Throwable) {
                        referenceResponse.postValue(Resource.Error(t.message))
                    }

                })
        }
    }

    fun getReference() : MutableLiveData<Resource<List<ReferenceResponseItem>>> {
        return referenceResponse
    }

    override fun onCleared() {
        super.onCleared()
        vmJob.cancel()
    }
}