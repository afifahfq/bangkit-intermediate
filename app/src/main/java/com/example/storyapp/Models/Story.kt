package com.example.storyapp.Models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Story(
    val name: String?,
    val description: String?,
    val photoUrl: String?
): Parcelable
