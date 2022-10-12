package com.example.eventer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.google.android.material.button.MaterialButton
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import fragments.EventChatFragment
import fragments.MembersFragment

class ShowEventActivity : AppCompatActivity() {
    private lateinit var place: TextView
    private lateinit var date: TextView
    private lateinit var title: TextView
    private lateinit var toolBar: Toolbar

    private var toggleBtn = false;
    private lateinit var eventId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_event)
        place = findViewById(R.id.place_event)
        date = findViewById(R.id.date_event)
        title = findViewById(R.id.title_event)

        toolBar = findViewById(R.id.tb_show_event)
        toolBar.setNavigationIcon(R.drawable.ic_close)
        toolBar.setNavigationOnClickListener  {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        val id = this.intent.getStringExtra("id")
        val docRef = Firebase.firestore.collection("events").document(id as String)
        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    place.text = document.data?.get("place").toString()
                    date.text = document.data?.get("date").toString()
                    title.text = document.data?.get("title").toString()
                    this.eventId = id
                } else {
                    Log.d("sd", "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d("sd", "get failed with ", exception)
            }

        replaceFragment(EventChatFragment())

        findViewById<MaterialButton>(R.id.btnMembers).setOnClickListener {
            if (this.toggleBtn) {
                replaceFragment(EventChatFragment())
            } else {
                val bundle = Bundle()
                bundle.putString("eventId", this.eventId)
                val fr = MembersFragment()
                fr.arguments = bundle
                replaceFragment(fr)
            }
            this.toggleBtn = !this.toggleBtn
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment)
        transaction.commit()
    }
}

