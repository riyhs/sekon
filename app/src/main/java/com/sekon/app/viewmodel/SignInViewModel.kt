package com.sekon.app.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
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

class SignInViewModel : ViewModel() {
    private var signInResponse = MutableLiveData<SignInResponse>()
    private var tokenResponse = MutableLiveData<String>()
    private var isSignInSuccess = MutableLiveData<Boolean>()

    private var vmJob = Job()
    private var scope = CoroutineScope(Dispatchers.Default + vmJob)

    fun setSignIn(data: DataSignIn) {
        scope.launch {
            NetworkConfig()
                .getService()
                .postSignIn(data)
                .enqueue(object : Callback<SignInResponse> {
                    override fun onResponse(
                        call: Call<SignInResponse>,
                        response: Response<SignInResponse>
                    ) {
                        isDataValid(response)
                    }

                    override fun onFailure(call: Call<SignInResponse>, t: Throwable) {
                        Log.d("SIGNIN", "Gagal login ${t.message}")
                    }
                })
        }
    }

    private fun isDataValid(response: Response<SignInResponse>) {
        if (response.body()?.status == "Sukses") {
            signInResponse.postValue(response.body())
            tokenResponse.postValue(response.body()?.token)
            isSignInSuccess.postValue(true)

        } else {
            isSignInSuccess.postValue(false)
        }
    }

    fun getSignInResponse() : LiveData<SignInResponse> = signInResponse
    fun getToken() : LiveData<String> = tokenResponse
    fun getSignInIsSuccess() : LiveData<Boolean> = isSignInSuccess

    override fun onCleared() {
        super.onCleared()
        vmJob.cancel()
    }
}