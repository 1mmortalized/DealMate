package com.bizsolutions.dealmate.ui.keywords

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bizsolutions.dealmate.databinding.ItemKeywordBinding
import com.bizsolutions.dealmate.db.KeywordEntity


class KeywordsRecViewAdapter() :
    ListAdapter<KeywordEntity, KeywordsRecViewAdapter.KeywordViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): KeywordViewHolder {
        val binding = ItemKeywordBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return KeywordViewHolder(binding)
    }

    override fun onBindViewHolder(holder: KeywordViewHolder, position: Int) {
        val keyword = getItem(position)
        val binding = holder.binding

        binding.itemKeywordText.text = keyword.word
        binding.itemKeywordCompletedCountTxt.text = keyword.completedCount.toString()
        binding.itemKeywordPostponedCountTxt.text = keyword.postponedCount.toString()
    }

    inner class KeywordViewHolder(val binding: ItemKeywordBinding) : ViewHolder(binding.root)

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<KeywordEntity>() {
            override fun areItemsTheSame(oldItem: KeywordEntity, newItem: KeywordEntity): Boolean {
                return oldItem.word == newItem.word
            }

            override fun areContentsTheSame(oldItem: KeywordEntity, newItem: KeywordEntity): Boolean {
                return oldItem == newItem
            }
        }
    }
}