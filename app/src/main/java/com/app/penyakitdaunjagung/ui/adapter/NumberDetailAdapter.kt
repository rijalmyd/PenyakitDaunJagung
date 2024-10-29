package com.app.penyakitdaunjagung.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.app.penyakitdaunjagung.databinding.ItemNumberContentBinding

class NumberDetailAdapter(
    private val isWhiteColor: Boolean = false
) : ListAdapter<String, NumberDetailAdapter.ViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(ItemNumberContentBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), position + 1)
    }

    inner class ViewHolder(private val binding: ItemNumberContentBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(content: String, number: Int) {
            binding.apply {
                tvNumber.text = "$number ."
                tvContentNumber.text = content
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil. ItemCallback<String>() {
            override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem == newItem
            }
        }
    }
}