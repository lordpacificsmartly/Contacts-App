package com.jesse.contactsexampleapp.ui

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.tasks.Tasks.call
import com.jesse.contactsexampleapp.R

class ContactDetailsActivity : AppCompatActivity() {

    //declare variables
    private lateinit var name: TextView
    private lateinit var phone: TextView
    private lateinit var call: ImageView
    private lateinit var share: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_details)

        //getting reference to view
        name = findViewById(R.id.textView)
        phone = findViewById(R.id.textView2)
        call = findViewById(R.id.imageView2)
        share = findViewById(R.id.imageView3 )

        var nameReceived = intent.getStringExtra("nameSentFromMain").toString()
        var phoneReceived = intent.getStringExtra("phoneSentFromMain")

        //setting values to textViews
        name.text = nameReceived
        phone.text = phoneReceived

        // Intent to start call
        call.setOnClickListener {
            val intent = Intent(
                Intent.ACTION_DIAL,
                Uri.parse("tel:" + Uri.encode(phoneReceived))
            )
            startActivity(intent)
        }


        // Intent to start share
            share.setOnClickListener {
            val name = nameReceived
            val phone = phoneReceived
            val shareIntent = Intent().apply {
                this.action = Intent.ACTION_SEND
                this.putExtra(Intent.EXTRA_TEXT, "$name\n$phone")
                this.type = "text/plain"
            }
            startActivity(shareIntent)
        }
    }

}