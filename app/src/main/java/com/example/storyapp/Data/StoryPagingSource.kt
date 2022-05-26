package com.example.storyapp.Data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.storyapp.Api.ApiService
import com.example.storyapp.Models.ListStoryItem
import retrofit2.awaitResponse

class StoryPagingSource(private val apiService: ApiService) : PagingSource<Int, ListStoryItem>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ListStoryItem> {
        return try {
            val page = params.key ?: INITIAL_PAGE_INDEX
            val auth = ApiService.TOKEN
            val responseData = apiService.getAllStories(auth, page, params.loadSize, 0).awaitResponse()
            val result = responseData.body()
            val listResult = result!!.listStory

            LoadResult.Page(
                data = listResult!!,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (listResult.isNullOrEmpty()) null else page + 1
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