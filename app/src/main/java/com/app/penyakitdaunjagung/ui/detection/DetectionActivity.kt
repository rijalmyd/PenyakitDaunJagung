package com.app.penyakitdaunjagung.ui.detection

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.app.penyakitdaunjagung.R
import com.app.penyakitdaunjagung.data.helper.ImageClassifierHelper
import com.app.penyakitdaunjagung.data.model.History
import com.app.penyakitdaunjagung.data.model.diseaseList
import com.app.penyakitdaunjagung.databinding.ActivityDetectionBinding
import com.app.penyakitdaunjagung.ml.JagungOrNot
import com.app.penyakitdaunjagung.ui.detail.DetailActivity
import com.app.penyakitdaunjagung.util.bitmapToBase64String
import com.app.penyakitdaunjagung.util.getCurrentDate
import com.app.penyakitdaunjagung.util.getImageUri
import com.app.penyakitdaunjagung.util.toBitmap
import com.app.penyakitdaunjagung.view_model.ViewModelFactory
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import id.zelory.compressor.Compressor
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.label.Category
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import org.tensorflow.lite.task.vision.classifier.Classifications
import kotlin.math.roundToInt

class DetectionActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityDetectionBinding.inflate(layoutInflater)
    }
    private var currentImageUri: Uri? = null
    private var isJagung: Boolean = true
    private lateinit var imageClassifierHelper: ImageClassifierHelper
    private val viewModel by viewModels<DetectionViewModel> {
        ViewModelFactory.getInstance(this.applicationContext)
    }
    private val loadingDialog by lazy {
        MaterialAlertDialogBuilder(this)
            .setTitle("Loading...")
            .setMessage("Sedang memproses gambar...")
            .setCancelable(false)
            .create()
    }
    private val notCornAlertDialog by lazy {
        MaterialAlertDialogBuilder(this)
            .setTitle("Warning!")
            .setMessage("Gambar bukan daun jagung, silahkan coba lagi")
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val modelAdapter = ArrayAdapter(
            this,
            R.layout.item_dropdown,
            resources.getStringArray(R.array.models)
        )
        imageClassifierHelper = ImageClassifierHelper(
            this,
            classifierListener = object : ImageClassifierHelper.ClassifierListener {
                override fun onError(error: String) {
                    runOnUiThread {
                        if (loadingDialog.isShowing) {
                            loadingDialog.dismiss()
                        }
                        showToast(error)
                    }
                }

                override fun onResults(results: List<Classifications>?, inferenceTime: Long) {
                    runOnUiThread {
                        if (!results.isNullOrEmpty()) {
                            if (results.isNotEmpty() && results[0].categories.isNotEmpty()) {
                                val category = results[0].categories.firstOrNull()
                                saveAndNavigate(category)
                            }
                        } else {
                            if (loadingDialog.isShowing) {
                                loadingDialog.dismiss()
                            }
                            showToast("Gambar tidak terklasifikasi sebagai penyakit daun jagung")
                        }
                    }
                }
            }
        )
        binding.apply {
            dropdownModel.setAdapter(modelAdapter)

            setSupportActionBar(toolbar)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)

            btnGallery.setOnClickListener { startGallery() }
            btnCamera.setOnClickListener { startCamera() }
            btnDetect.setOnClickListener { detectImage() }
        }
    }

    private fun saveAndNavigate(category: Category?) {
        if (category != null) {
            lifecycleScope.launch {
                val bitmap = lifecycleScope.async {
                    bitmapToBase64String(
                        bmp = currentImageUri?.toBitmap(this@DetectionActivity) ?: throw Exception("Bitmap null"),
                        format = Bitmap.CompressFormat.WEBP,
                        quality = 20
                    )
                }
                val disease = diseaseList.find {
                    it.classId.equals(
                        category.label?.trim(),
                        ignoreCase = true
                    )
                }

                if (isJagung) {
                    val history = History(
                        date = getCurrentDate(),
                        percentage = (category.score * 100).roundToInt(),
                        imageBitmapConverted = bitmap.await(),
                        modelName = binding.dropdownModel.text.toString(),
                        diseaseName = disease?.name ?: "",
                        isJagung = isJagung,
                        diseaseNameLatin = disease?.nameLatin ?: "",
                        diseaseTreatment = disease?.treatment ?: emptyList(),
                        diseaseImageResourceId = disease?.imageResourceId ?: 0
                    )
                    viewModel.insertHistory(history).observe(this@DetectionActivity) { id ->
                        navigateToDetail(id)
                        if (loadingDialog.isShowing) {
                            loadingDialog.dismiss()
                        }
                    }
                } else {
                    if (loadingDialog.isShowing) {
                        loadingDialog.dismiss()
                    }
                    if (!notCornAlertDialog.isShowing) {
                        notCornAlertDialog.show()
                    }
                }
            }
        }
    }

    private fun navigateToDetail(id: Long) {
        val intent = Intent(this, DetailActivity::class.java).apply {
            putExtra(DetailActivity.EXTRA_TYPE, DetailActivity.TYPE_HISTORY)
            putExtra(DetailActivity.EXTRA_HISTORY, id)
        }
        startActivity(intent)
    }

    private suspend fun getCornOrNot(uri: Uri) {
        val bitmap = uri.toBitmap(this)
        val scaledBitmap = Bitmap.createScaledBitmap(bitmap, 150, 150, false)

        val jagungOrNotModel = JagungOrNot.newInstance(this)

        val tBuffer = TensorImage(DataType.FLOAT32)
        tBuffer.load(scaledBitmap)
        val byteBuffer = tBuffer.buffer
        val inputFeature0 =
            TensorBuffer.createFixedSize(intArrayOf(1, 150, 150, 3), DataType.FLOAT32)
        inputFeature0.loadBuffer(byteBuffer)

        val outputs = jagungOrNotModel.process(inputFeature0)
        val outputFeature0 = outputs.outputFeature0AsTensorBuffer
        val output = outputFeature0.floatArray

        isJagung = output[0] == 0.0f
    }

    private fun detectImage() {
        loadingDialog.show()
        val model = binding.dropdownModel.text.toString()
        when (model) {
            "MobileNet" -> {
                lifecycleScope.launch {
                    currentImageUri?.let {
                        getCornOrNot(it)
                        imageClassifierHelper.classifyStaticImage(it)
                    } ?: run {
                        showToast("Pilih gambar terlebih dahulu!")
                    }
                }
            }

            "AlexNet" -> {

            }

            else -> showToast("Model belum dipilih")
        }
    }

    private fun startCamera() {
        currentImageUri = getImageUri(this)
        if (currentImageUri != null) {
            launcherIntentCamera.launch(currentImageUri!!)
        }
    }

    private fun startGallery() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            type = "image/*"
        }
        launcherGallery.launch(intent)
    }

    private fun showImage() {
        currentImageUri?.let {
            Glide.with(this)
                .load(it)
                .encodeQuality(20)
                .into(binding.ivClassify)
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        val uri = it.data?.data
        if (uri != null) {
            currentImageUri = uri

            val contentResolver = applicationContext.contentResolver
            val takeFlags =
                Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
            currentImageUri?.let { contentResolver.takePersistableUriPermission(it, takeFlags) }
            showImage()
        } else {
            Log.d("Photo Picker", "No media selected")
            showToast("No media selected")
        }
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            showImage()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}