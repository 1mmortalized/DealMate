package com.bizsolutions.dealmate.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bizsolutions.dealmate.databinding.ItemCallBinding
import com.bizsolutions.dealmate.db.CallWithClient
import com.bizsolutions.dealmate.db.TaskWithClient
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle


class CallRecViewAdapter(
    private val onEditMenuItemClicked: (TaskWithClient) -> Unit,
    private val onDeleteMenuItemClicked: (TaskWithClient) -> Unit
) :
    ListAdapter<CallWithClient, CallRecViewAdapter.CallViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CallViewHolder {
        val binding =
            ItemCallBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CallViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CallViewHolder, position: Int) {
        val call = getItem(position)
        val binding = holder.binding

        binding.itemCallTitleTxt.text = call.call.title
        binding.itemCallClientTxt.text = call.client.name
        binding.itemCallTimeTxt.text = call.call.time.format(
            DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)
        )
//        binding.itemTaskCheckbox.isChecked = task.task.completed



//        val popupMenu = PopupMenu(context, holder.binding.itemBookQuoteMenuBtn)
//        popupMenu.menuInflater.inflate(R.menu.quote_item_menu, popupMenu.menu)
//        popupMenu.setupOptionalIcons(context)
//
//        popupMenu.setOnMenuItemClickListener { menuItem ->
//            when (menuItem.itemId) {
//                R.id.quote_item_menu_item_edit -> onEditMenuItemClicked(quote)
//                R.id.quote_item_menu_item_copy -> {
//                    val clipboard: ClipboardManager? =
//                        context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager?
//
//                    clipboard?.let {
//                        val clip = ClipData.newPlainText("quote_text_clip", quote.text)
//                        clipboard.setPrimaryClip(clip)
//                    }
//                }
//
//                R.id.quote_item_menu_item_select_text -> onSelectTextMenuItemClicked(quote.text)
//                R.id.quote_item_menu_item_delete -> onDeleteMenuItemClicked(quote)
//            }
//
//            true
//        }
//
//        holder.binding.itemBookQuoteMenuBtn.setOnClickListener {
//            popupMenu.show()
//        }


    }


    inner class CallViewHolder(val binding: ItemCallBinding) : ViewHolder(binding.root)

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<CallWithClient>() {
            override fun areItemsTheSame(oldItem: CallWithClient, newItem: CallWithClient): Boolean {
                return oldItem.call.id == newItem.call.id
            }

            override fun areContentsTheSame(oldItem: CallWithClient, newItem: CallWithClient): Boolean {
                return oldItem == newItem
            }
        }
    }
}