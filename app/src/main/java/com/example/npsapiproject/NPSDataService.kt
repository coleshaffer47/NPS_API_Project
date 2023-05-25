package com.example.npsapiproject

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface NPSDataService {
    // ?parkCode=acad,dena"  query so use @Query("key")
    // @Header("X-Api-Key: ")
    @GET("parks")
    fun getNationalPark(
        @Query("api_key") apiKey: String,
        @Query("parkCode", encoded = true) parkCode: String,
        @Query("limit") limit: Int
    ) : Call<NPSDataWrapper>

    @GET("alerts")
    fun getAlerts(
        @Query("api_key") apiKey: String,
        @Query("parkCode", encoded = true) parkCode: String,
        @Query("limit") limit: Int
    ) : Call<NPSAlertDataWrapper>

    @GET("passportstamplocations")
    fun getStamps(
        @Query("api_key") apiKey: String,
        @Query("parkCode", encoded = true) parkCode: String,
        @Query("limit") limit: Int
    ) : Call<NPSStampDataWrapper>



}