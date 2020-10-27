package com.sekon.app.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sekon.app.model.covid.CovidResponse
import com.sekon.app.model.covid.CovidResponseItem
import com.sekon.app.network.NetworkConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CovidViewModel : ViewModel() {

    private var covidData = MutableLiveData<CovidResponseItem>()
    private var vmJob = Job()
    private var scope = CoroutineScope(Dispatchers.Default + vmJob)

    fun setCovidInfo(from: String, to: String) {
        scope.launch {
            NetworkConfig()
                .getServiceCovid()
                .getCovidInfo(from, to)
                .enqueue(object : Callback<CovidResponse> {
                    override fun onResponse(
                        call: Call<CovidResponse>,
                        response: Response<CovidResponse>
                    ) {
                        Log.d("COVID", response.body().toString())
                        val lastIndex = response.body()!!.size - 1
                        covidData.postValue(response.body()?.get(lastIndex))
                    }

                    override fun onFailure(call: Call<CovidResponse>, t: Throwable) {
                        Log.e("COVID", t.message.toString())
                    }
                })
        }
    }

    fun getCovidInfo() : LiveData<CovidResponseItem>{
        return covidData
    }

    override fun onCleared() {
        super.onCleared()
        vmJob.cancel()
    }

}