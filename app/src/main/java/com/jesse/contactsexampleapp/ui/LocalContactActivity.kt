package com.jesse.contactsexampleapp.ui

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jesse.contactsexampleapp.ItemClickedListener
import com.jesse.contactsexampleapp.R
import com.jesse.contactsexampleapp.data.Contact

class LocalContactActivity : AppCompatActivity(), ItemClickedListener {

    private val READ_CONTACTS = 102
    private lateinit var localContactRecyclerView: RecyclerView
    lateinit var localContactAdapter: LocalContactAdapter
    private var contactArrayList = ArrayList<Contact>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_local_contact)

        localContactRecyclerView = findViewById(R.id.rvLocalContact)
        checkForPermissions(android.Manifest.permission.READ_CONTACTS, "read_contacts", READ_CONTACTS)
    }

    // checking for permissions
    private fun checkForPermissions(permission: String, name: String, requestCode: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            when {
                ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED -> {
                    readContacts()
                }
                shouldShowRequestPermissionRationale(permission) -> showDialog(permission, name, requestCode)
                else -> ActivityCompat.requestPermissions(this, arrayOf(permission), requestCode)
            }
        }
    }

    // for permission request
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        fun innerCheck(name: String) {
            if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(applicationContext, "$name permission refused", Toast.LENGTH_SHORT).show()
            } else {
                readContacts()
                Toast.makeText(applicationContext, "$name permission granted SHOULD CALL", Toast.LENGTH_SHORT).show()
            }
        }
        when (requestCode) {
            READ_CONTACTS -> innerCheck("read_contacts")
        }
    }
    // dialog to request user permission
    private fun showDialog(permission: String, name: String, requestCode: Int) {
        val builder = AlertDialog.Builder(this)
        builder.apply {
            setMessage("Permission to access your $name is required to use this app")
            setTitle("Permission required")
            setPositiveButton("OK") { dialog, which ->
                ActivityCompat.requestPermissions(this@LocalContactActivity, arrayOf(permission), requestCode)
            }
        }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
    // to read local contacts
    private fun readContacts() {
        val contacts = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null)
        var count = 0
        while (contacts!!.moveToNext()) {
            val name = contacts.getString(contacts.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
            val PhoneNumber = contacts.getString(contacts.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
            val obj = Contact()
            obj.fullName = name
            obj.contactNumber = PhoneNumber
            contactArrayList.add(obj)
            count++
        }
        localContactAdapter = LocalContactAdapter(contactArrayList, this)
        localContactRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        localContactRecyclerView.adapter = localContactAdapter
        contacts.close()
    }

    override fun onRecyclerViewItemClicked(id: String?, name: String?, phoneNumber: String?) {
        val intent = Intent(this, ContactDetailsActivity::class.java)
        intent.putExtra("nameSentFromMain", "$name")
        intent.putExtra("idSentFromMain", "$id")
        intent.putExtra("phoneSentFromMain", "$phoneNumber")

        startActivity(intent)
    }
}
