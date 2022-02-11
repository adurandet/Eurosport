package com.eurosport.home.network

import com.google.gson.GsonBuilder
import com.eurosport.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiHelper {

    private fun getApiInterface(apiClass: Class<out ApiInterface>): ApiInterface {
        val headerInterceptor = Interceptor { chain ->
            val request = chain.request()
            val builder = request.newBuilder()
            builder.header("Content-Type", "application/json")
            chain.proceed(builder.build())
        }

        val httpClient = OkHttpClient.Builder()
            .addInterceptor(headerInterceptor)
            .build()

        val gson = GsonBuilder().create()

        val retrofit: Retrofit = Retrofit.Builder()
            .client(httpClient)
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        return retrofit.create(apiClass)
    }

    fun getHomeApiInterface() = getApiInterface(HomeApiInterface::class.java) as HomeApiInterface
}