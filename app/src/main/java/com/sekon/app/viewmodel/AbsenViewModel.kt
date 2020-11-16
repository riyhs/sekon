package com.sekon.app.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sekon.app.model.Resource
import com.sekon.app.model.absen.PostAbsenBody
import com.sekon.app.model.absen.PostAbsenResponse
import com.sekon.app.network.NetworkConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AbsenViewModel: ViewModel() {
    private var job = Job()
    private var scope = CoroutineScope(Dispatchers.Default + job)

    private var absenResponse = MutableLiveData<Resource<PostAbsenResponse>>()

    fun postAbsen(absenBody: PostAbsenBody) {
        scope.launch {
            absenResponse.postValue(Resource.Loading())

            NetworkConfig()
                .getService()
                .postAbsen(absenBody)
                .enqueue(object : Callback<PostAbsenResponse> {
                    override fun onResponse(
                        call: Call<PostAbsenResponse>,
                        response: Response<PostAbsenResponse>
                    ) {
                        absenResponse.postValue(Resource.Success(response.body()))
                    }

                    override fun onFailure(call: Call<PostAbsenResponse>, t: Throwable) {
                        absenResponse.postValue(Resource.Error(t.message))
                    }

                })
        }
    }

    fun getPostAbsenResponse(): MutableLiveData<Resource<PostAbsenResponse>> {
        return absenResponse
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}