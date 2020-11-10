package com.sekon.app.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sekon.app.model.Resource
import com.sekon.app.model.announcement.AnnouncementPostModel
import com.sekon.app.model.announcement.AnnouncementPostResponse
import com.sekon.app.network.NetworkConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DashboardAnnouncementViewModel: ViewModel() {
    private val pengumumanResponse = MutableLiveData<Resource<AnnouncementPostResponse>>()

    private var vmJob = Job()
    private var scope = CoroutineScope(Dispatchers.Default + vmJob)

    fun setPengumuman(announcement : AnnouncementPostModel) {
        scope.launch {
            pengumumanResponse.postValue(Resource.Loading())

            NetworkConfig()
                .getService()
                .postPengumuman(announcement)
                .enqueue(object : Callback<AnnouncementPostResponse>{
                    override fun onResponse(
                        call: Call<AnnouncementPostResponse>,
                        response: Response<AnnouncementPostResponse>
                    ) {
                        pengumumanResponse.postValue(Resource.Success(response.body()))
                    }

                    override fun onFailure(call: Call<AnnouncementPostResponse>, t: Throwable) {
                        pengumumanResponse.postValue(Resource.Error(t.message))
                    }
                })

        }
    }

    fun getPengumuman(): MutableLiveData<Resource<AnnouncementPostResponse>> {
        return pengumumanResponse
    }

    override fun onCleared() {
        super.onCleared()
        vmJob.cancel()
    }
}