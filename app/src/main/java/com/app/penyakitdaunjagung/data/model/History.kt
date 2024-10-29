package com.app.penyakitdaunjagung.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity
data class History(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val percentage: Int = 0,
    val date: String = "",
    val modelName: String = "",
    val isJagung: Boolean = true,
    val imageBitmapConverted: String? = null,
    val diseaseName: String = "",
    val diseaseNameLatin: String = "",
    val diseaseTreatment: List<String> = emptyList(),
    val diseaseImageResourceId: Int? = null
)