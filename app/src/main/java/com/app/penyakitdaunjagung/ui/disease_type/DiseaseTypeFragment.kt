package com.app.penyakitdaunjagung.ui.disease_type

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.penyakitdaunjagung.R
import com.app.penyakitdaunjagung.data.model.diseaseList
import com.app.penyakitdaunjagung.databinding.FragmentDiseaseTypeBinding
import com.app.penyakitdaunjagung.ui.adapter.DiseaseAdapter
import com.app.penyakitdaunjagung.ui.detail.DetailActivity

class DiseaseTypeFragment : Fragment() {

    private var _binding: FragmentDiseaseTypeBinding? = null
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentDiseaseTypeBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val diseaseAdapter = DiseaseAdapter(
            onItemClick = { disease ->
                val intent = Intent(requireActivity(), DetailActivity::class.java).apply {
                    putExtra(DetailActivity.EXTRA_TYPE, DetailActivity.TYPE_DISEASE)
                    putExtra(DetailActivity.EXTRA_DISEASE, disease)
                }
                startActivity(intent)
            }
        )

        binding?.apply {
            rvDisease.adapter = diseaseAdapter
            rvDisease.layoutManager = LinearLayoutManager(requireActivity())
        }

        diseaseAdapter.submitList(diseaseList)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}