package com.jesse.contactsexampleapp

interface ItemClickedListener {
    fun onRecyclerViewItemClicked(
        id: String?,
        name: String?,
        phoneNumber: String?
    )
}