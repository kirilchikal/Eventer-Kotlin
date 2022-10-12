package com.example.eventer

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.*
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso

class MyFriends : AppCompatActivity() {

    private lateinit var toolBar: Toolbar
    private lateinit var friendsList: RecyclerView
    private var dbref = Firebase.firestore.collection("users")
    private var auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_friends)


        toolBar = findViewById(R.id.toolbar_my_friends)
        friendsList = findViewById(R.id.rv_friends_list)

        //setSupportActionBar(tool_bar)
        toolBar.setNavigationIcon(R.drawable.ic_back)
        toolBar.setNavigationOnClickListener  {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("activity", 2)
            startActivity(intent)
        }

        friendsList.setHasFixedSize(true)
        friendsList.layoutManager = LinearLayoutManager(this)
        val data = ArrayList<SearchFriends>()
        dbref.document(auth.uid!!).get().addOnSuccessListener {
            if (it?.get("friends") != null) {
                var friends: List<String> = it?.get("friends") as List<String>
                for (id in friends) {
                    dbref.document(id).get().addOnSuccessListener { doc ->
                        if (doc != null) {
                            data.add(SearchFriends(id, doc.get("email").toString(),doc.get("fullname").toString()))
                        }
                    }.addOnCompleteListener {
                        val adapter = FriendsAdapter(data)
                        friendsList.adapter = adapter
                    }
                }
            }
        }
    }
}

class FriendsAdapter(private val mList: List<SearchFriends>) : RecyclerView.Adapter<FriendsAdapter.ViewHolderFriends>() {

    private val storageReference = FirebaseStorage.getInstance().reference
    private var auth = FirebaseAuth.getInstance()
    private var database = Firebase.firestore

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderFriends {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.friend_card_delete, parent, false)

        return ViewHolderFriends(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolderFriends, position: Int) {
        val searchFriends = mList[position]

        val pathRef = storageReference.child("users/" + searchFriends.uid + "/profile.jpg")
        pathRef.downloadUrl.addOnSuccessListener {
            if (it != null) {
                Picasso.get().load(it).into(holder.image)
            }
        }.addOnFailureListener {
            holder.image.setBackgroundResource(R.drawable.prof_im)
        }

        holder.fullName.text = searchFriends.fullName
        holder.mail.text = searchFriends.mail

        holder.btn_delete.setOnClickListener{
            database.collection("users").document(auth.uid!!)
                .update("friends", FieldValue.arrayRemove(searchFriends.uid))
            holder.btn_delete.isEnabled = false
            holder.btn_delete.isClickable = false
            holder.btn_delete.setBackgroundColor(Color.parseColor("#00C89D"))
            holder.btn_delete.setTextColor(Color.parseColor("#FFFFFF"))
        }
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    // Holds the views for adding it to image and text
    class ViewHolderFriends(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.fcd_image)
        val fullName: TextView = itemView.findViewById(R.id.event_fullname)
        val mail: TextView = itemView.findViewById(R.id.event_email)
        val btn_delete: MaterialButton = itemView.findViewById(R.id.fcd_delete_btn)
    }
}