package com.jesse.contactsexampleapp.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.jesse.contactsexampleapp.data.Contact
import com.jesse.contactsexampleapp.data.NODE_CONTACTS

class ContactViewModel: ViewModel() {
    //initialize variable to firebase database
    private val dbcontacts = FirebaseDatabase.getInstance().getReference(NODE_CONTACTS)

    private val _result = MutableLiveData<Exception?>()
    val result: LiveData<Exception?> get() = _result

    private val _contact = MutableLiveData<Contact>()
    val contact: LiveData<Contact> get() = _contact


    //method to add contact
    fun addContact(contact: Contact){

        //id will be used for reference purpose,
        //for example when you need to update or delete or call the contact object
        contact.id = dbcontacts.push().key

        //save id to contact generated
        dbcontacts.child(contact.id!!).setValue(contact).addOnCompleteListener {
            if (it.isSuccessful){
                _result.value = null
            } else {
                _result.value = it.exception
            }
        }
    }

    private val childEventListener = object: ChildEventListener{
        override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
           val contact = snapshot.getValue(Contact::class.java)
            contact?.id = snapshot.key
            _contact.value = contact!!
        }

        override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
            val contact = snapshot.getValue(Contact::class.java)
            contact?.id = snapshot.key
            _contact.value = contact!!
        }

        override fun onChildRemoved(snapshot: DataSnapshot) {
            val contact = snapshot.getValue(Contact::class.java)
            contact?.id = snapshot.key
            contact?.isDeleted = true
            _contact.value = contact!!
        }

        override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}

        override fun onCancelled(error: DatabaseError) {}

    }
    //methods used for adding, deleting, updating, and displaying data
    fun getRealtimeUpdate(){
        dbcontacts.addChildEventListener(childEventListener)
    }

    fun updateContact(contact: Contact){
        dbcontacts.child(contact.id!!).setValue(contact)
            .addOnCompleteListener {
                if (it.isSuccessful){
                    _result.value = null
                }else{
                    _result.value = it.exception
                }
            }
    }

    fun deleteContact(contact: Contact){
        dbcontacts.child(contact.id!!).setValue(null)
            .addOnCompleteListener {
                if (it.isSuccessful){
                    _result.value = null
                }else{
                    _result.value = it.exception
                }
            }
    }

    override fun onCleared() {
        super.onCleared()
        dbcontacts.removeEventListener(childEventListener)
    }



}