package com.bizsolutions.dealmate.ui.contacts

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.children
import androidx.core.view.isInvisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bizsolutions.dealmate.databinding.ItemContactBinding
import com.bizsolutions.dealmate.db.ClientEntity
import com.wynneplaga.materialScrollBar2.inidicators.AlphabeticIndicator


class ContactRecViewAdapter(
    private val onEditMenuItemClicked: (ClientEntity) -> Unit,
    private val onDeleteMenuItemClicked: (ClientEntity) -> Unit
) :
    ListAdapter<ContactListItem, ContactRecViewAdapter.ContactViewHolder>(DiffCallback), AlphabeticIndicator.INameableAdapter {

    fun groupSubmitList(list: List<ClientEntity>?) {
        val groupedList = list?.let { groupContactsByFirstLetter(it) }
        super.submitList(groupedList)
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is ContactListItem.Header -> TYPE_HEADER
            is ContactListItem.Contact -> TYPE_CONTACT
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ContactViewHolder {
        val binding =
            ItemContactBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ContactViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val binding = holder.binding
        when (val item = getItem(position)) {
            is ContactListItem.Header -> {
                for (view in binding.root.children) {
                    view.isInvisible = true
                }
                binding.itemContactHeaderTxt.isInvisible = false

                binding.itemContactHeaderTxt.text = item.letter
            }
            is ContactListItem.Contact -> {
                for (view in binding.root.children) {
                    view.isInvisible = false
                }
                binding.itemContactHeaderTxt.isInvisible = true

                binding.itemContactName.text = item.client.name
                binding.itemContactPhone.text = item.client.phone
                binding.itemContactEmail.text = item.client.email
            }
        }
    }

    inner class ContactViewHolder(val binding: ItemContactBinding) : ViewHolder(binding.root)

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<ContactListItem>() {
            override fun areItemsTheSame(oldItem: ContactListItem, newItem: ContactListItem): Boolean {
                return when {
                    oldItem is ContactListItem.Header && newItem is ContactListItem.Header ->
                        oldItem.letter == newItem.letter
                    oldItem is ContactListItem.Contact && newItem is ContactListItem.Contact ->
                        oldItem.client.id == newItem.client.id
                    else -> false
                }
            }

            override fun areContentsTheSame(oldItem: ContactListItem, newItem: ContactListItem): Boolean {
                return oldItem == newItem
            }
        }

        private const val TYPE_HEADER = 0
        private const val TYPE_CONTACT = 1
    }

    override fun getCharacterForElement(element: Int): Char {
        val item = getItem(element)
        val itemText = when (item) {
            is ContactListItem.Contact -> item.client.name
            is ContactListItem.Header -> item.letter
        }
        return itemText.firstOrNull()?.uppercaseChar() ?: '#'
    }

    private fun groupContactsByFirstLetter(clients: List<ClientEntity>): List<ContactListItem> {
        return clients
            .sortedBy { it.name.lowercase() }
            .groupBy { it.name.firstOrNull()?.uppercaseChar()?.toString() ?: "#" }
            .toSortedMap()
            .flatMap { (letter, clientGroup) ->
                listOf(ContactListItem.Header(letter)) +
                        clientGroup.map { ContactListItem.Contact(it) }
            }
    }
}