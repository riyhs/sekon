package com.sekon.app.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sekon.app.model.Resource
import com.sekon.app.model.signin.DataSignIn
import com.sekon.app.model.signin.SignInResponse
import com.sekon.app.network.NetworkConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


@Suppress("BlockingMethodInNonBlockingContext")
class SignInViewModel : ViewModel() {
    private var signInResponse = MutableLiveData<Resource<SignInResponse>>()

    private var vmJob = Job()
    private var scope = CoroutineScope(Dispatchers.Default + vmJob)

    fun setSignIn(data: DataSignIn) {
        signInResponse.postValue(Resource.Loading())

        scope.launch {
            NetworkConfig()
                .getService()
                .postSignIn(data)
                .enqueue(object : Callback<SignInResponse> {
                    override fun onResponse(
                        call: Call<SignInResponse>,
                        response: Response<SignInResponse>
                    ) {
                        signInResponse.postValue(Resource.Success(response.body()))
                        Log.d("RESPONSE", response.body().toString())
                    }

                    override fun onFailure(call: Call<SignInResponse>, t: Throwable) {
                        signInResponse.postValue(Resource.Error(t.message))
                    }
                })
        }
    }

    fun getSignInResponse() : LiveData<Resource<SignInResponse>> = signInResponse

    override fun onCleared() {
        super.onCleared()
        vmJob.cancel()
    }
}