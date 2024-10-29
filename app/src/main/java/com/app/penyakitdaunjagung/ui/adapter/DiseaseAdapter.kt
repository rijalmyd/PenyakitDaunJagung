package com.app.penyakitdaunjagung.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.app.penyakitdaunjagung.data.model.Disease
import com.app.penyakitdaunjagung.databinding.ItemDiseaseBinding
import com.bumptech.glide.Glide

class DiseaseAdapter(
    private val onItemClick: (Disease) -> Unit
) : ListAdapter<Disease, DiseaseAdapter.DiseaseViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DiseaseAdapter.DiseaseViewHolder {
        return DiseaseViewHolder(ItemDiseaseBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: DiseaseAdapter.DiseaseViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class DiseaseViewHolder(private val binding: ItemDiseaseBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(disease: Disease) {
            with(binding) {
                Glide.with(itemView.context)
                    .load(disease.imageResourceId)
                    .into(ivLogo)
                tvTitle.text = disease.name
                tvDesc.text = disease.indication
                itemView.setOnClickListener { onItemClick(disease) }
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Disease>() {
            override fun areItemsTheSame(oldItem: Disease, newItem: Disease): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Disease, newItem: Disease): Boolean {
                return oldItem.classId == newItem.classId
            }
        }
    }
}