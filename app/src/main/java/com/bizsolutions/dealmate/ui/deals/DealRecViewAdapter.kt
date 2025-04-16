package com.bizsolutions.dealmate.ui.deals

import android.text.Spannable
import android.text.SpannableString
import android.text.style.RelativeSizeSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bizsolutions.dealmate.databinding.ItemDealBinding
import com.bizsolutions.dealmate.databinding.ItemDealHeaderBinding
import com.bizsolutions.dealmate.db.DealWithClient
import com.bizsolutions.dealmate.db.EventEntity
import java.text.DecimalFormat
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Locale


class DealRecViewAdapter(
    private val onEditMenuItemClicked: (EventEntity) -> Unit,
    private val onDeleteMenuItemClicked: (EventEntity) -> Unit
) :
    ListAdapter<DealListItem, ViewHolder>(DiffCallback) {

    fun groupSubmitList(list: List<DealWithClient>?) {
        val groupedList = list?.let { groupDealsByDate(it) }
        super.submitList(groupedList)
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is DealListItem.Header -> TYPE_HEADER
            is DealListItem.Deal -> TYPE_DEAL
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {

        return when(viewType) {
            TYPE_HEADER -> {
                val binding =
                    ItemDealHeaderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                DealHeaderViewHolder(binding)
            }
            else -> {
                val binding =
                    ItemDealBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                DealViewHolder(binding)
            }
        }

        val binding =
            ItemDealBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DealViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (val item = getItem(position)) {
            is DealListItem.Header -> {
                val binding = (holder as DealHeaderViewHolder).binding

                val formatter = DateTimeFormatter.ofPattern("LLLL yyyy", Locale.getDefault())
                val monthTitle = item.date.format(formatter).replaceFirstChar { it.titlecase() }

                binding.itemDealHeaderTxt.text = monthTitle
            }
            is DealListItem.Deal -> {
                val binding = (holder as DealViewHolder).binding
                val deal = item.deal.deal
                val client = item.deal.client

                binding.itemDealTitleTxt.text = deal.title
                binding.itemDealDateTxt.text =
                    deal.date.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT))
                binding.itemDealClientTxt.text = client.name

                val decimalFormat = DecimalFormat("#,###")
                val formattedAmount = decimalFormat.format(deal.amount)

                val amount = "%s %s".format(formattedAmount, deal.currency.uppercase())
                val spannableAmount = SpannableString(amount)
                spannableAmount.setSpan(
                    RelativeSizeSpan(0.7f),
                    amount.indexOf(deal.currency.uppercase()),
                    amount.length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                binding.itemDealAmountTxt.text = spannableAmount

                binding.itemDealDivider.alpha =
                    if (position > 0 && getItem(position - 1) is DealListItem.Header) 1f else 0.2f
            }
        }
    }

    inner class DealViewHolder(val binding: ItemDealBinding) : ViewHolder(binding.root)
    inner class DealHeaderViewHolder(val binding: ItemDealHeaderBinding) : ViewHolder(binding.root)

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<DealListItem>() {
            override fun areItemsTheSame(oldItem: DealListItem, newItem: DealListItem): Boolean {
                return when {
                    oldItem is DealListItem.Header && newItem is DealListItem.Header ->
                        oldItem.date == newItem.date
                    oldItem is DealListItem.Deal && newItem is DealListItem.Deal ->
                        oldItem.deal.deal.id == newItem.deal.deal.id
                    else -> false
                }
            }

            override fun areContentsTheSame(oldItem: DealListItem, newItem: DealListItem): Boolean {
                return oldItem == newItem
            }
        }

        private const val TYPE_HEADER = 0
        private const val TYPE_DEAL = 1
    }

    private fun groupDealsByDate(deals: List<DealWithClient>): List<DealListItem> {
        return deals
            .sortedBy { it.deal.date }
            .groupBy { it.deal.date.withDayOfMonth(1) }
            .toSortedMap(compareByDescending { it })
            .flatMap { (monthStartDate, dealGroup) ->
                listOf(DealListItem.Header(monthStartDate)) +
                        dealGroup.map { DealListItem.Deal(it) }
            }
    }
}