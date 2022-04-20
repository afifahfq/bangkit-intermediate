package com.example.storyapp.ViewModels

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.storyapp.Api.ApiConfig
import com.example.storyapp.Models.BaseResponse
import com.example.storyapp.Models.LoginResponse
import com.example.storyapp.Models.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserViewModel : ViewModel(){
    val userViewModelStatus = MutableLiveData<Boolean>()
    val registerStatus = MutableLiveData<Boolean>()
    var mUser = MutableLiveData<User>()

    fun login(email: String, password: String) {
        userViewModelStatus.postValue(true)
        val client = ApiConfig.getApiService().login(email, password)
        client.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(
                call: Call<LoginResponse>,
                response: Response<LoginResponse>
            ) {
                val responseBody = response.body()
                if (responseBody != null) {
                    val result = responseBody.loginResult
                    var user = User(
                        result?.userId,
                        result?.name,
                        result?.token
                    )
                    mUser.postValue(user)
                    userViewModelStatus.postValue(false)
                }
            }
            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Log.e("UserViewModel", "onFailure: ${t.message}")
            }
        })
    }

    fun register(name: String, email: String, password: String) {
        userViewModelStatus.postValue(true)
        val client = ApiConfig.getApiService().register(name, email, password)
        client.enqueue(object : Callback<BaseResponse> {
            override fun onResponse(
                call: Call<BaseResponse>,
                response: Response<BaseResponse>
            ) {
                val responseBody = response.body()
                if (responseBody != null) {
                    val result = responseBody.message
                    userViewModelStatus.postValue(false)
                    if (result == "User Created") {
                        registerStatus.postValue(true)
                        login(email, password)
                    }
                    else {
                        registerStatus.postValue(false)
                    }
                }
            }
            override fun onFailure(call: Call<BaseResponse>, t: Throwable) {
                Log.e("UserViewModel", "onFailure: ${t.message}")
            }
        })
    }

    fun getUser(): LiveData<User>? {
        return mUser
    }

    fun getStatus(): MutableLiveData<Boolean> {
        return userViewModelStatus
    }
}