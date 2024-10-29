package com.app.penyakitdaunjagung.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.app.penyakitdaunjagung.data.api.ApiService
import com.app.penyakitdaunjagung.data.model.History
import com.app.penyakitdaunjagung.data.model.WeatherResponse
import com.app.penyakitdaunjagung.data.room.HistoryDao

class HomeViewModel(
    private val apiService: ApiService,
    private val historyDao: HistoryDao,
) : ViewModel() {

    fun getCurrentWeather(latLong: String): LiveData<WeatherResponse> = liveData {
        try {
            val result = apiService.getCurrentWeather(latLong = latLong)
            emit(result)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getHistories(): LiveData<List<History>> = historyDao.getHistories()
}