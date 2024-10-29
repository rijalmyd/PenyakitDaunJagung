package com.app.penyakitdaunjagung.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Base64
import androidx.core.content.FileProvider
import com.app.penyakitdaunjagung.BuildConfig
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.format
import id.zelory.compressor.constraint.quality
import id.zelory.compressor.constraint.size
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun getCurrentDate(): String {
    val sdf = SimpleDateFormat("dd-MM-yyyy hh:mm:ss", Locale.getDefault())
    return sdf.format(Date())
}

fun bitmapToBase64String(bmp: Bitmap, format: CompressFormat, quality: Int): String? {
    val outputStream = ByteArrayOutputStream()
    bmp.compress(format, quality, outputStream)
    val bytes = outputStream.toByteArray()
    return Base64.encodeToString(bytes, Base64.DEFAULT)
}

fun base64StringToBitmap(base64Str: String?): Bitmap? {
    val decodedString = Base64.decode(base64Str, Base64.DEFAULT)
    return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
}

fun getImageUri(context: Context): Uri? {
    return try {
        val cachePath = File(context.cacheDir, "images")
        if (!cachePath.exists()) cachePath.mkdirs()

        val imageFile = File(cachePath, "${getCurrentDate()}.jpg")

        FileProvider.getUriForFile(
            context,
            "${BuildConfig.APPLICATION_ID}.provider",
            imageFile
        )
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

suspend fun File.compressImageFile(context: Context): File {
    return Compressor.compress(context, this) {
        format(Bitmap.CompressFormat.WEBP)
        quality(20)
        size(100000)
    }
}

@Suppress("DEPRECATION")
suspend fun Uri.toBitmap(context: Context): Bitmap = withContext(Dispatchers.IO) {
    val contentResolver = context.contentResolver
    return@withContext if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        val source = ImageDecoder.createSource(contentResolver, this@toBitmap)
        ImageDecoder.decodeBitmap(source).copy(Bitmap.Config.ARGB_8888, true)
    } else {
        MediaStore.Images.Media.getBitmap(contentResolver, this@toBitmap)
    }
//    val byteArray = bitmapToByteArray(bitmap)
//    return@withContext if (byteArray.size > 1024 * 1024) { // If larger than 1 MB, compress it
//        compressBitmapToMaxSize(bitmap, 1000 * 1024) // Compress to max 100 KB
//    } else {
//        bitmap // If below 1 MB, return the original bitmap
//    }
}

suspend fun compressBitmapToMaxSize(bitmap: Bitmap, maxSize: Int): Bitmap = withContext(Dispatchers.IO) {
    var compressQuality = 100 // Start at the highest quality
    val stream = ByteArrayOutputStream()

    // Compress the bitmap and decrease quality until it fits within the desired size
    bitmap.compress(Bitmap.CompressFormat.WEBP, compressQuality, stream)
    var byteArray = stream.toByteArray()

    while (byteArray.size > maxSize && compressQuality > 0) {
        stream.reset() // Reset the stream to apply a new compression level
        compressQuality -= 5 // Decrease quality by 5%
        bitmap.compress(Bitmap.CompressFormat.WEBP, compressQuality, stream)
        byteArray = stream.toByteArray()
    }

    BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
}

fun bitmapToByteArray(bitmap: Bitmap): ByteArray {
    val stream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.WEBP, 100, stream)
    return stream.toByteArray()
}

fun getImageUriFromBitmap(context: Context, bitmap: Bitmap): Uri? {
    return try {
        val cachePath = File(context.cacheDir, "images")
        if (!cachePath.exists()) cachePath.mkdirs()

        val file = File(cachePath, "image_${System.currentTimeMillis()}.jpg")
        val outputStream = file.outputStream()

        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream)
        outputStream.flush()
        outputStream.close()

        FileProvider.getUriForFile(context, "${BuildConfig.APPLICATION_ID}.provider", file)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}
