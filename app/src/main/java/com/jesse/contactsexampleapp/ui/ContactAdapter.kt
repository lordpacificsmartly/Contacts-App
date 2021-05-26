package com.jesse.contactsexampleapp.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jesse.contactsexampleapp.ItemClickedListener
import com.jesse.contactsexampleapp.data.Contact
import com.jesse.contactsexampleapp.databinding.RecyclerViewContactBinding


class ContactAdapter (private val listener : ItemClickedListener): RecyclerView.Adapter<ContactAdapter.ViewHolder>() {

    //initialize variable to mutable list of type contact class
    var contacts = mutableListOf<Contact>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        //inflate recycler view layout
        return ViewHolder(RecyclerViewContactBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ContactAdapter.ViewHolder, position: Int) {

        //declare and initialize variables
        val idToSend = contacts[position].id
        val nameToSend = contacts[position].fullName
        val phoneToSend = contacts[position].contactNumber


        //bind name and phone text views and initialize to contacts
        holder.binding.tvName.text = contacts[position].fullName
        holder.binding.tvContact.text = contacts[position].contactNumber


        holder.itemView.setOnClickListener {
            listener.onRecyclerViewItemClicked(idToSend, nameToSend, phoneToSend)
        }
    }

    override fun getItemCount(): Int {
       //return the size of contacts
        return contacts.size
    }

    fun addContact(contact: Contact){
        if (!contacts.contains(contact)){
            contacts.add(contact)
        }else{
            val index=contacts.indexOf(contact)
            if (contact.isDeleted){
                contacts.removeAt(index)
            }else{
                contacts[index] = contact
            }
        }

        notifyDataSetChanged()
    }

    //create interface
    interface ContactListener {
        fun onContactClick(position:Int)
    }

    //ViewHolder inner class
    inner class ViewHolder(val binding: RecyclerViewContactBinding): RecyclerView.ViewHolder(binding.root){

    }
}