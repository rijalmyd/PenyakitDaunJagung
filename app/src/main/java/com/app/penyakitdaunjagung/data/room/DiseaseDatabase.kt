package com.app.penyakitdaunjagung.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.app.penyakitdaunjagung.data.model.History

@Database(
    entities = [History::class],
    version = 1,
    exportSchema = true
)
@TypeConverters(ListTypeConverter::class)
abstract class DiseaseDatabase : RoomDatabase() {

    abstract fun historyDao(): HistoryDao

    companion object {
        @Volatile
        private var INSTANCE: DiseaseDatabase? = null

        fun getInstance(context: Context): DiseaseDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DiseaseDatabase::class.java,
                    "disease_database"
                ).fallbackToDestructiveMigration()
                    .build()
                instance.also { INSTANCE = it }
            }
        }
    }
}