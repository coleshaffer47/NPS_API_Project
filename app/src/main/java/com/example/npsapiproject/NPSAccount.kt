package com.example.npsapiproject

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.text.SimpleDateFormat
import java.util.*

@Parcelize
data class NPSAccount (
    var hasBadge: MutableMap<String, Boolean>? = mutableMapOf(),
    var dateVisited: MutableMap<String, String>? = mutableMapOf(),
    var personalNote: MutableMap<String, String>? = mutableMapOf(),
    var isLoggedIn: Boolean = false,
    var objectId : String? = null,
    var ownerId : String? = null
) : Parcelable {

//    init {
//        hadBadge = makeBooleanMap()
//        personalNote = makeStringMap()
//        dateVisited = makeDateMap()
//    }

    fun makeMap() {
        val i = 0
        val myFormat = "MM/dd/yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        var stringMap = mutableMapOf<String,String>()
        var booleanMap = mutableMapOf<String,Boolean>()
        var dateMap = mutableMapOf<String,String>()

        while (i < Constants.PARK_LIST.size) {
            booleanMap[Constants.PARK_LIST[i]] = false
            stringMap[Constants.PARK_LIST[i]] = ""
            dateMap[Constants.PARK_LIST[i]] = ""
        }
        hasBadge = booleanMap
        dateVisited = dateMap
        personalNote = stringMap
    }

//    fun makeStringMap() : MutableMap<String, String> {
//        val i = 0
//        var stringMap = mutableMapOf<String,String>()
//        while (i < Constants.PARK_LIST.size) {
//            stringMap[Constants.PARK_LIST[i]] = ""
//        }
//        return stringMap
//    }
//
//    fun makeDateMap() : MutableMap<String, String> {
//        val myFormat = "MM/dd/yyyy"
//        val sdf = SimpleDateFormat(myFormat, Locale.US)
//
//        val i = 0
//        var dateMap = mutableMapOf<String,String>()
//        while (i < Constants.PARK_LIST.size) {
////            dateMap[Constants.PARK_LIST[i]] = sdf.format(Calendar.getInstance().time)
//            dateMap[Constants.PARK_LIST[i]] = ""
//        }
//        return dateMap
//
//    }
}

