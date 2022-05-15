package com.example.storyapp.Helper

import android.content.Context
import com.example.storyapp.Api.ApiConfig
import com.example.storyapp.Data.StoryRepository
import com.example.storyapp.Database.StoryDatabase

object Injection {
    fun provideRepository(context: Context): StoryRepository {
        val database = StoryDatabase.getDatabase(context)
        val apiService = ApiConfig.getApiService()
        return StoryRepository(database, apiService)
    }
}