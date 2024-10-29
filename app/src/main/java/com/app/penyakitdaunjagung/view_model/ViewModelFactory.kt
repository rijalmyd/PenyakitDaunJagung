package com.app.penyakitdaunjagung.view_model

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.app.penyakitdaunjagung.data.api.ApiConfig
import com.app.penyakitdaunjagung.data.api.ApiService
import com.app.penyakitdaunjagung.data.room.DiseaseDatabase
import com.app.penyakitdaunjagung.data.room.HistoryDao
import com.app.penyakitdaunjagung.ui.detail.DetailViewModel
import com.app.penyakitdaunjagung.ui.detection.DetectionViewModel
import com.app.penyakitdaunjagung.ui.home.HomeViewModel
import com.app.penyakitdaunjagung.ui.main.MainViewModel

class ViewModelFactory private constructor(
    private val apiService: ApiService,
    private val historyDao: HistoryDao,
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(apiService, historyDao) as T
            }
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(apiService, historyDao) as T
            }
            modelClass.isAssignableFrom(DetectionViewModel::class.java) -> {
                DetectionViewModel(historyDao) as T
            }
            modelClass.isAssignableFrom(DetailViewModel::class.java) -> {
                DetailViewModel(historyDao) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null

        fun getInstance(context: Context): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(
                    apiService = ApiConfig.createApiService(),
                    historyDao = DiseaseDatabase.getInstance(context).historyDao()
                ).also {
                    instance = it
                }
            }
    }
}