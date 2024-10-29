package com.app.penyakitdaunjagung.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.app.penyakitdaunjagung.data.model.History
import com.app.penyakitdaunjagung.data.room.HistoryDao

class DetailViewModel(
    private val historyDao: HistoryDao
) : ViewModel() {

    fun getHistoryById(id: Long): LiveData<History?> = historyDao.getHistoryById(id)
}