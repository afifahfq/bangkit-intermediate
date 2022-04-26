package com.example.storyapp.Models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.time.LocalDateTime

@Parcelize
data class Story(
    val id: String?,
    val name: String?,
    val description: String?,
    val photoUrl: String?,
//    val createdAt: LocalDateTime?,
    val createdAt: String?,
    val lat: Double?,
    val lon: Double?
): Parcelable
