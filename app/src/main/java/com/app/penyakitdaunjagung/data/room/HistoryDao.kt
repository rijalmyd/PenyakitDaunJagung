package com.app.penyakitdaunjagung.data.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.app.penyakitdaunjagung.data.model.History

@Dao
interface HistoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(history: History): Long

    @Query("SELECT * FROM history WHERE id = :id")
    fun getHistoryById(id: Long): LiveData<History?>

    @Query("SELECT * FROM history ORDER BY date DESC")
    fun getHistories(): LiveData<List<History>>
}