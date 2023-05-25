package com.example.npsapiproject

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class NPSAlert(
    val title: String,
    val description: String,
    val url: String,
    val category: String
) : Parcelable
