package com.example.storyapp.Api

import com.example.storyapp.Models.BaseResponse
import com.example.storyapp.Models.LoginResponse
import com.example.storyapp.Models.StoriesResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
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

    @Headers("Authorization: Bearer <Personal Access Token>")
    @Multipart
    @POST("stories")
    fun addNewStory(
        @Field("description") name: String,
        @Part("photo") photo: MultipartBody.Part,
        @Field("lat") lat: Float? = null,
        @Field("lon") lon: Float? = null
    ): Call<BaseResponse>

    // apakah bisa upload file tanpa multipart
//    @Multipart
    @POST("stories/guest")
    fun addNewStoryGuest(
        @Field("description") name: String,
        @Part("photo") photo: MultipartBody.Part,
        @Field("lat") lat: Float? = null,
        @Field("lon") lon: Float? = null
    ): Call<BaseResponse>

//    @Headers("Authorization: Bearer <Personal Access Token>")
    @GET("stories")
    fun getAllStories(
        @Header("Authorization") auth: String?,
        @Query("page") page: Int? = null,
        @Query("size") size: Int? = null,
        @Query("location") location: Int = 0
    ): Call<StoriesResponse>
}