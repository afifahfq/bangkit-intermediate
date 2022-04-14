package com.example.storyapp.Models

import com.google.gson.annotations.SerializedName

data class BaseResponse(

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)
