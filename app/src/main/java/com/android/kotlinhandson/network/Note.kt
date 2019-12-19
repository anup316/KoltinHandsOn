package com.android.kotlinhandson.network

import com.google.gson.annotations.SerializedName

data class Note(
    @SerializedName("note_title") val title: String,
    @SerializedName("note_desc") val description: String,
    @SerializedName("date_modified") val dateModified: String
)