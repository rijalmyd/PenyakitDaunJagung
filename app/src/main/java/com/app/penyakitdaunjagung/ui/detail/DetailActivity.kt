package com.app.penyakitdaunjagung.ui.detail

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.IntentCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.penyakitdaunjagung.R
import com.app.penyakitdaunjagung.data.model.Disease
import com.app.penyakitdaunjagung.data.model.diseaseList
import com.app.penyakitdaunjagung.databinding.ActivityDetailBinding
import com.app.penyakitdaunjagung.ui.adapter.NumberDetailAdapter
import com.app.penyakitdaunjagung.util.base64StringToBitmap
import com.app.penyakitdaunjagung.view_model.ViewModelFactory
import com.bumptech.glide.Glide

class DetailActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityDetailBinding.inflate(layoutInflater)
    }
    private val numberDetailAdapter by lazy {
        NumberDetailAdapter()
    }
    private val viewModel by viewModels<DetailViewModel> {
        ViewModelFactory.getInstance(this.applicationContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val type = intent.getIntExtra(EXTRA_TYPE, 0)

        binding.rvTreatment.apply {
            layoutManager = LinearLayoutManager(this@DetailActivity)
            adapter = numberDetailAdapter
        }

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        when (type) {
            TYPE_DISEASE -> { showDiseaseDetail() }
            TYPE_HISTORY -> { showHistoryDetail() }
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    @SuppressLint("SetTextI18n")
    private fun showDiseaseDetail() {
        val disease = IntentCompat.getParcelableExtra(intent, EXTRA_DISEASE, Disease::class.java)
        binding.apply {
            toolbar.title = "Detail Penyakit"

            if (disease?.classId?.equals("sehat", ignoreCase = true) == true) {
                tvLatin.visibility = View.GONE
                textIndication.text = "Ciri-ciri"
                rvTreatment.visibility = View.GONE
                textTreatment.visibility = View.GONE
            }

            Glide.with(this@DetailActivity)
                .load(disease?.imageResourceId)
                .into(ivDetail)

            tvTitle.text = disease?.name
            tvLatin.text = disease?.nameLatin
            tvIndication.text = disease?.indication

            numberDetailAdapter.submitList(disease?.treatment)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun showHistoryDetail() {
        val historyId = intent.getLongExtra(EXTRA_HISTORY, 0)

        binding.apply {
            toolbar.title = "Hasil Deteksi"
            textIndication.visibility = View.GONE
            tvIndication.visibility = View.GONE

            viewModel.getHistoryById(historyId).observe(this@DetailActivity) { history ->

                binding.cvCornOrNot.isVisible = history?.isJagung == false
                if (history?.diseaseName?.equals("sehat", ignoreCase = true) == true) {
                    tvLatin.visibility = View.GONE
                    textIndication.text = "Ciri-ciri"
                    rvTreatment.visibility = View.GONE

                    textTreatment.visibility = View.GONE
                    textIndication.visibility = View.VISIBLE
                    tvIndication.visibility = View.VISIBLE
                    tvIndication.text = diseaseList.find {
                        it.classId.equals("sehat", ignoreCase = true)
                    }?.indication
                }

                Glide.with(this@DetailActivity)
                    .load(base64StringToBitmap(history?.imageBitmapConverted))
                    .into(ivDetail)

                tvTitle.text = "${history?.diseaseName} ${history?.percentage}%"
                tvLatin.text = history?.diseaseNameLatin

                numberDetailAdapter.submitList(history?.diseaseTreatment)
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }

    companion object {
        const val EXTRA_DISEASE = "extra_disease"
        const val EXTRA_HISTORY = "extra_history"
        const val EXTRA_TYPE = "extra_type"
        const val TYPE_DISEASE = 1
        const val TYPE_HISTORY = 0
    }
}