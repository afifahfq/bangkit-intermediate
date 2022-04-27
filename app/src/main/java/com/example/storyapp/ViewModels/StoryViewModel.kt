package com.example.storyapp.ViewModels

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.storyapp.Api.ApiConfig
import com.example.storyapp.Models.BaseResponse
import com.example.storyapp.Models.StoriesResponse
import com.example.storyapp.Models.Story
import com.example.storyapp.Preferences.UserPreference
import okhttp3.MultipartBody
import okhttp3.Request
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class StoryViewModel : ViewModel(){
    val mLoading = MutableLiveData<Boolean>()
    val mList = MutableLiveData<ArrayList<Story>>()
    val list = ArrayList<Story>()
    val mUpload = MutableLiveData<Boolean>()

    fun getAllStories(auth: String?, page: Int?, size: Int?, location: Int) {
        mLoading.postValue(true)
        val client = ApiConfig.getApiService().getAllStories(auth, page, size, location)
        client.enqueue(object : Callback<StoriesResponse> {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onResponse(
                call: Call<StoriesResponse>,
                response: Response<StoriesResponse>
            ) {
                val responseBody = response.body()
                val error = responseBody?.error
                if (error == false) {
                    for ( story in responseBody.listStory!!) {
                        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
//                        val currdatetime = LocalDateTime.parse(story?.createdAt, formatter)

                        var story = Story(
                            story?.id,
                            story?.name,
                            story?.description,
                            story?.photoUrl,
                            story?.createdAt,
                            story?.lat,
                            story?.lon
                        )
                        list.add(story)
                    }
                    mLoading.postValue(false)
                    mList.postValue(list)
                }
            }
            override fun onFailure(call: Call<StoriesResponse>, t: Throwable) {
                Log.e("StoryViewModel", "onFailure: ${t.message}")
            }
        })
    }

    fun addNewStory(auth: String?, description: RequestBody, photo: MultipartBody.Part) {
        mLoading.postValue(true)
        val client = ApiConfig.getApiService().addNewStory(auth, description, photo)
        client.enqueue(object : Callback<BaseResponse> {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onResponse(
                call: Call<BaseResponse>,
                response: Response<BaseResponse>
            ) {
                val responseBody = response.body()
                val error = responseBody?.error
                if (error == false) {
                    mLoading.postValue(false)
                    mUpload.postValue(true)
                }
            }
            override fun onFailure(call: Call<BaseResponse>, t: Throwable) {
                Log.e("StoryViewModel", "onFailure: ${t.message}")
                mUpload.postValue(false)
            }
        })
    }

    fun addNewStoryGuest(description: RequestBody, photo: MultipartBody.Part) {
        mLoading.postValue(true)
        val client = ApiConfig.getApiService().addNewStoryGuest(description, photo)
        client.enqueue(object : Callback<BaseResponse> {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onResponse(
                call: Call<BaseResponse>,
                response: Response<BaseResponse>
            ) {
                val responseBody = response.body()
                val error = responseBody?.error
                if (error == false) {
                    mLoading.postValue(false)
                    mUpload.postValue(true)
                }
            }
            override fun onFailure(call: Call<BaseResponse>, t: Throwable) {
                Log.e("StoryViewModel", "onFailure: ${t.message}")
                mUpload.postValue(false)
            }
        })
    }

    fun getList(): LiveData<ArrayList<Story>?> {
        return mList
    }

    fun getStatus(): MutableLiveData<Boolean> {
        return mUpload
    }

    fun getLoading(): MutableLiveData<Boolean> {
        return mLoading
    }

}