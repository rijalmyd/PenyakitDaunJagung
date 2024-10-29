package com.app.penyakitdaunjagung.ui.main

import androidx.lifecycle.ViewModel
import com.app.penyakitdaunjagung.data.api.ApiService
import com.app.penyakitdaunjagung.data.room.HistoryDao

class MainViewModel(
    private val apiService: ApiService,
    private val historyDao: HistoryDao,
) : ViewModel() {


}