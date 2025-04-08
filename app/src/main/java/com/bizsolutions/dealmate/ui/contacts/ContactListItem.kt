package com.bizsolutions.dealmate.ui.contacts

import com.bizsolutions.dealmate.db.ClientEntity

sealed class ContactListItem {
    data class Header(val letter: String) : ContactListItem()
    data class Contact(val client: ClientEntity) : ContactListItem()
}