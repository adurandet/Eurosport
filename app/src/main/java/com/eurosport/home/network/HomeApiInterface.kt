package com.eurosport.home.network

import com.eurosport.home.network.response.GetHomeContentResponse
import retrofit2.http.GET

interface HomeApiInterface: ApiInterface {
    @GET("api/json-storage/bin/edfefba")
    suspend fun getHomeContent(): GetHomeContentResponse
}