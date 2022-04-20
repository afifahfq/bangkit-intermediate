package com.example.storyapp.Models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
    var userId: String? = null,
    var name: String? = null,
    var token: String? = null
):Parcelable
