package com.app.penyakitdaunjagung.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.app.penyakitdaunjagung.data.model.History
import com.app.penyakitdaunjagung.databinding.ItemHistoryBinding
import com.app.penyakitdaunjagung.util.base64StringToBitmap
import com.bumptech.glide.Glide

class HistoryAdapter(
    private val onItemClick: (History) -> Unit
) : ListAdapter<History, HistoryAdapter.HistoryViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HistoryAdapter.HistoryViewHolder {
        return HistoryViewHolder(ItemHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: HistoryAdapter.HistoryViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class HistoryViewHolder(private val binding: ItemHistoryBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(history: History) {
            binding.apply {
                tvDate.text = history.date
                tvTitle.text = "${history.diseaseName} ${history.percentage}%"
                tvModelName.text = history.modelName
                Glide.with(itemView.context)
                    .load(base64StringToBitmap(history.imageBitmapConverted))
                    .into(ivLogo)
            }
            itemView.setOnClickListener {
                onItemClick(history)
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<History>() {
            override fun areItemsTheSame(oldItem: History, newItem: History): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: History, newItem: History): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}