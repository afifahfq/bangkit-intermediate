package com.example.storyapp.Data

import android.os.Build
import android.security.identity.AccessControlProfileId
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.storyapp.Api.ApiConfig
import com.example.storyapp.Api.ApiService
import com.example.storyapp.Models.ListStoryItem
import com.example.storyapp.Models.StoriesResponse
import com.example.storyapp.Models.Story
import com.example.storyapp.ViewModels.StoryViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.awaitResponse
import java.time.format.DateTimeFormatter

class StoryPagingSource(private val apiService: ApiService) : PagingSource<Int, ListStoryItem>() {
    private lateinit var mLiveDataList: StoryViewModel
    val list = ArrayList<Story>()

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ListStoryItem> {
        return try {
            val page = params.key ?: INITIAL_PAGE_INDEX
            val auth = ApiService.TOKEN
            val responseData = apiService.getAllStories(auth, page, params.loadSize, 0).awaitResponse()
            val result = responseData.body()
            val listResult = result!!.listStory

            Log.i("CEKRESPON", responseData.toString())

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
                            var curr = Story(
                                story!!.id!!,
                                story?.name,
                                story?.description,
                                story?.photoUrl,
                                story?.createdAt,
                                story?.lat,
                                story?.lon
                            )
                            list.add(curr)
                            Log.i("CEKRESULT", list.toString())
                        }
                    }
                }
                override fun onFailure(call: Call<StoriesResponse>, t: Throwable) {
                    Log.e("StoryViewModel", "onFailure: ${t.message}")
                }
            })

            Log.i("CEKRESULTFINAL", list.toString())
//            val json = "[{"photoUrl":"https://story-api.dicoding.dev/images/stories/photos-1652951553186_wsd-dGuy.jpg", "createdAt":"2022-05-19T09:12:33.188Z", "name":"user2", "description":"test hi", "lon":-122.084, "id":"story-3yMxUXDW3aHlu6t9", "lat":37.421998333333335}]"

            LoadResult.Page(
                data = listResult!!,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (list.isNullOrEmpty()) null else page + 1
            )
        } catch (exception: Exception) {
            return LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, ListStoryItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }
}