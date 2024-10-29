package com.app.penyakitdaunjagung.ui.detection

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.app.penyakitdaunjagung.data.model.History
import com.app.penyakitdaunjagung.data.model.diseaseList
import com.app.penyakitdaunjagung.data.room.HistoryDao
import com.app.penyakitdaunjagung.util.bitmapToBase64String
import com.app.penyakitdaunjagung.util.getCurrentDate
import kotlinx.coroutines.launch
import org.tensorflow.lite.support.label.Category
import kotlin.math.roundToInt

class DetectionViewModel(
    private val historyDao: HistoryDao,
) : ViewModel() {

    fun insertHistory(history: History): LiveData<Long> = liveData{
        try {
            val insertedId = historyDao.insert(history)
            emit(insertedId)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}