package com.example.storyapp.ViewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.storyapp.Api.ApiConfig
import com.example.storyapp.Models.Story
import retrofit2.Callback

class StoryViewModel {
    val userViewModelStatus = MutableLiveData<Boolean>()
    val mList = MutableLiveData<ArrayList<Story>>()
    val list = ArrayList<Story>()

    fun findUsers(username: String) {
        list.clear()
        mList.postValue(list)

        userViewModelStatus.postValue(true)
        val client = ApiConfig.getApiService().searchUsers(username)
        client.enqueue(object : Callback<SearchResponse> {
            override fun onResponse(
                call: Call<SearchResponse>,
                response: Response<SearchResponse>
            ) {
                val responseBody = response.body()
                if (responseBody != null) {
                    for (user in responseBody.items!!) {
                        var curr = Story(
                            user?.login,
                            user?.htmlUrl,
                            user?.avatarUrl
                        )
                        list.add(curr)
                    }
                    userViewModelStatus.postValue(false)
                    mList.postValue(list)
                }
            }
            override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                Log.e("UserViewModel", "onFailure: ${t.message}")
            }
        })
    }

    fun getList(): LiveData<ArrayList<Story>?> {
        return mList
    }

    fun getStatus(): MutableLiveData<Boolean> {
        return userViewModelStatus
    }
}