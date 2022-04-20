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
    val loginStatus = MutableLiveData<Boolean>()
    var mUser = MutableLiveData<User>()
//    var mMessage = MutableLiveData<String>()

    fun login(email: String, password: String) {
        userViewModelStatus.postValue(true)
        val client = ApiConfig.getApiService().login(email, password)
        client.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(
                call: Call<LoginResponse>,
                response: Response<LoginResponse>
            ) {
                val responseBody = response.body()
                val error = responseBody?.error
                if (error == false) {
                    val result = responseBody.loginResult
                    var user = User(
                        result?.userId,
                        result?.name,
                        result?.token
                    )
                    mUser.postValue(user)
                    loginStatus.postValue(true)
                }
                else {
                    loginStatus.postValue(false)
//                    mMessage.postValue(message!!)
                }
                userViewModelStatus.postValue(false)
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
                val result = responseBody?.error
                if (result == false) {
                    loginStatus.postValue(true)
//                        login(email, password)
                }
                else {
                    loginStatus.postValue(false)
//                    mMessage.postValue(message!!)
                    userViewModelStatus.postValue(false)
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

    fun getLoading(): MutableLiveData<Boolean> {
        return userViewModelStatus
    }

    fun getStatus(): MutableLiveData<Boolean> {
        return loginStatus
    }

//    fun getMessage(): MutableLiveData<String> {
//        return mMessage
//    }
}