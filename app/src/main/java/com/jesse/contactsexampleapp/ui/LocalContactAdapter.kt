package com.jesse.contactsexampleapp.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.jesse.contactsexampleapp.ItemClickedListener
import com.jesse.contactsexampleapp.data.Contact
import com.jesse.contactsexampleapp.databinding.LocalContactItemBinding


class LocalContactAdapter(var contacts: ArrayList<Contact>, private val listener: ItemClickedListener) :
    RecyclerView.Adapter<LocalContactAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        //inflate recycler view layout
        return ViewHolder(
            LocalContactItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: LocalContactAdapter.ViewHolder, position: Int) {
        val idToSend = contacts[position].id
        val nameToSend = contacts[position].fullName
        val phoneToSend = contacts[position].contactNumber


        holder.binding.tvLocalName.text = contacts[position].fullName
        holder.binding.tvLocalContact.text = contacts[position].contactNumber


        holder.itemView.setOnClickListener {
            listener.onRecyclerViewItemClicked(idToSend, nameToSend, phoneToSend)
        }
    }

    override fun getItemCount(): Int {
        //return the size of contacts
        return contacts.size
    }

    fun addContact(contact: Contact) {
        if (!contacts.contains(contact)) {
            contacts.add(contact)
        } else {
            val index = contacts.indexOf(contact)
            if (contact.isDeleted) {
                contacts.removeAt(index)
            } else {
                contacts[index] = contact
            }
        }

        notifyDataSetChanged()
    }

    interface ContactListener {
        fun onContactClick(position: Int)
    }

    //ViewHolder inner class
    inner class ViewHolder(val binding: LocalContactItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
    }
}