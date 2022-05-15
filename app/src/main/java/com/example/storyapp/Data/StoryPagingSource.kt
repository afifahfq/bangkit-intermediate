package com.example.storyapp.Data

import android.os.Build
import android.security.identity.AccessControlProfileId
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.storyapp.Api.ApiConfig
import com.example.storyapp.Api.ApiService
import com.example.storyapp.Models.StoriesResponse
import com.example.storyapp.Models.Story
import com.example.storyapp.ViewModels.StoryViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.format.DateTimeFormatter

class StoryPagingSource(private val apiService: ApiService) : PagingSource<Int, Story>() {
    private lateinit var mLiveDataList: StoryViewModel
    val list = ArrayList<Story>()

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Story> {
        return try {
            val page = params.key ?: INITIAL_PAGE_INDEX
            val auth = ApiService.TOKEN

            val client = ApiConfig.getApiService().getAllStories(auth, page, params.loadSize, 0)
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
                            var story = Story(
                                story!!.id!!,
                                story?.name,
                                story?.description,
                                story?.photoUrl,
                                story?.createdAt,
                                story?.lat,
                                story?.lon
                            )
                            list.add(story)
                        }
                    }
                }
                override fun onFailure(call: Call<StoriesResponse>, t: Throwable) {
                    Log.e("StoryViewModel", "onFailure: ${t.message}")
                }
            })

            Log.i("CEKRESULT", list.toString())

            LoadResult.Page(
                data = list,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (list.isNullOrEmpty()) null else page + 1
            )
        } catch (exception: Exception) {
            return LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Story>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }
}