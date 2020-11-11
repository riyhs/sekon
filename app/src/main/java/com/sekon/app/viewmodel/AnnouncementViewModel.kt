package com.sekon.app.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sekon.app.model.Resource
import com.sekon.app.model.announcement.AnnouncementResponse
import com.sekon.app.network.NetworkConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AnnouncementViewModel : ViewModel() {
    private var announcement = MutableLiveData<Resource<AnnouncementResponse>>()

    private var vmJob = Job()
    private var scope = CoroutineScope(Dispatchers.Default + vmJob)

    fun setAnnouncement() {

        announcement.postValue(Resource.Loading())

        scope.launch {
            NetworkConfig()
                .getService()
                .getAnnouncement()
                .enqueue(object : Callback<AnnouncementResponse> {
                    override fun onResponse(
                        call: Call<AnnouncementResponse>,
                        response: Response<AnnouncementResponse>
                    ) {
                        announcement.postValue(Resource.Success(response.body()))
                    }

                    override fun onFailure(call: Call<AnnouncementResponse>, t: Throwable) {
                        announcement.postValue(Resource.Error("Pastikan kamu terhubung internet"))
                    }
                })
        }
    }

    fun getAnnouncement(): MutableLiveData<Resource<AnnouncementResponse>> {
        return announcement
    }

    override fun onCleared() {
        super.onCleared()
        vmJob.cancel()
    }
}