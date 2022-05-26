package com.example.storyapp.Api

import com.example.storyapp.Models.BaseResponse
import com.example.storyapp.Models.LoginResponse
import com.example.storyapp.Models.StoriesResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    var token: String

    @FormUrlEncoded
    @POST("register")
    fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<BaseResponse>

    @FormUrlEncoded
    @POST("login")
    fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<LoginResponse>

    @Multipart
    @POST("stories")
    fun addNewStory(
        @Header("Authorization") auth: String? = Companion.TOKEN,
        @Part("description") name: RequestBody,
        @Part photo: MultipartBody.Part
    ): Call<BaseResponse>

    @Multipart
    @POST("stories/guest")
    fun addNewStoryGuest(
        @Part("description") name: RequestBody,
        @Part photo: MultipartBody.Part
    ): Call<BaseResponse>

    @GET("stories")
    fun getAllStories(
        @Header("Authorization") auth: String? = Companion.TOKEN,
        @Query("page") page: Int? = null,
        @Query("size") size: Int? = null,
        @Query("location") location: Int
    ): Call<StoriesResponse>

    companion object {
        var TOKEN = "TOKEN"
    }
}