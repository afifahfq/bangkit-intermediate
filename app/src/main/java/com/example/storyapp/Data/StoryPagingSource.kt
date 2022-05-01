package com.example.storyapp.Data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.storyapp.Api.ApiService
import com.example.storyapp.Models.Story
import com.example.storyapp.ViewModels.StoryViewModel

class StoryPagingSource(private val apiService: ApiService) : PagingSource<Int, Story>() {
    private lateinit var mLiveDataList: StoryViewModel

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Story> {
        return try {
            val page = params.key ?: INITIAL_PAGE_INDEX
            val auth = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJ1c2VyLXNFZjBRZHZrOTFJaXRZMEMiLCJpYXQiOjE2NTE0MTkxMzh9.l9SNq1ff7IQvZLysDMSmWLwbTt-h6wKJ08VS0f5Rg-w"
            mLiveDataList.getAllStories(auth, page, params.loadSize, 1)
//            val responseData = apiService.getAllStories(page, params.loadSize)
            val responseData = mLiveDataList.getDataList()

            LoadResult.Page(
                data = responseData,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (responseData.isNullOrEmpty()) null else page + 1
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
}