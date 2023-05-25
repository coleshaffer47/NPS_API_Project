package com.example.npsapiproject

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class NPSStamp (
    var label: String
) : Parcelable