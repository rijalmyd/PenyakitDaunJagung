package com.app.penyakitdaunjagung.data.model

import android.net.Uri
import android.os.Parcelable
import androidx.annotation.DrawableRes
import kotlinx.parcelize.Parcelize

@Parcelize
data class Disease(
    val classId: String = "",
    val name: String = "",
    val nameLatin: String = "",
    @DrawableRes val imageResourceId: Int? = null,
    val indication: String = "",
    val treatment: List<String> = emptyList()
) : Parcelable