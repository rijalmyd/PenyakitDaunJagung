package com.app.penyakitdaunjagung.data.api

import com.app.penyakitdaunjagung.data.model.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("current.json")
    suspend fun getCurrentWeather(
        @Query("key") key: String = "b148aa2459d84398b28151107240410",
        @Query("q") latLong: String
    ): WeatherResponse
}