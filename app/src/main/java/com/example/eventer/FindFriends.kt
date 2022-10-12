package com.example.eventer

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.*
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso

class FindFriends : AppCompatActivity() {

    private lateinit var toolBar: androidx.appcompat.widget.Toolbar
    private lateinit var searchInputText: EditText
    private lateinit var searchButton: ImageButton
    private lateinit var searchResultList: RecyclerView
    private lateinit var dbref: CollectionReference
    private var auth = FirebaseAuth.getInstance()
    private var dbUsers = Firebase.firestore.collection("users")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_find_friends)

        toolBar = findViewById(R.id.toolbar_find_friends)
        searchInputText = findViewById(R.id.et_find_friend_input)
        searchButton = findViewById(R.id.find_friends_btn)
        searchResultList = findViewById(R.id.rv_users_list)
        dbref = Firebase.firestore.collection("users")


        //setSupportActionBar(tool_bar)
        toolBar.setNavigationIcon(R.drawable.ic_back)
        toolBar.setNavigationOnClickListener  {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("activity", 2)
            startActivity(intent)
        }

        searchResultList.setHasFixedSize(true)
        searchResultList.layoutManager = LinearLayoutManager(this)


        //Load users data from snapshots
        searchButton.setOnClickListener {
            searchFriends(searchInputText.text.toString())
        }
    }

    private fun searchFriends(searchInput: String) {
        val data = ArrayList<SearchFriends>()
        var friends: List<String>
        //list of friends to not show them in search result
        dbUsers.document(auth.uid!!).get().addOnSuccessListener {
            if (it.get("friends") != null) {
                friends = it.get("friends") as List<String>      // list of friends
            }
            else {
                friends = listOf(auth.uid!!)
            }

            val query = dbref.orderBy("fullname").startAt(searchInput).endAt(searchInput + "\uf8ff")
            query.get().addOnSuccessListener {
                for (doc in it!!) {
                    if (doc.id != auth.uid && !friends.contains(doc.id))
                        data.add(SearchFriends(doc.id, doc.get("email").toString(),doc.get("fullname").toString()))
                }
            }.addOnCompleteListener {
                val adapter = CustomAdapter(data)
                searchResultList.adapter = adapter
            }
        }
    }
}


class CustomAdapter(private val mList: List<SearchFriends>) : RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

    private val storageReference = FirebaseStorage.getInstance().reference
    private var auth = FirebaseAuth.getInstance()
    private var database = Firebase.firestore

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.user_card_add, parent, false)

        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val searchFriends = mList[position]

        val pathRef = storageReference.child("users/" + searchFriends.uid + "/profile.jpg")     //path users/userid/profile.jpg
        pathRef.downloadUrl.addOnSuccessListener {
            if (it != null) {
                Picasso.get().load(it).into(holder.image)
            }
        }.addOnFailureListener {
            holder.image.setBackgroundResource(R.drawable.prof_im)
        }

        holder.fullName.text = searchFriends.fullName
        holder.mail.text = searchFriends.mail

        holder.btnAdd.setOnClickListener{
            database.collection("users").document(auth.uid!!)
                .update("friends", FieldValue.arrayUnion(searchFriends.uid))
            holder.btnAdd.isEnabled = false
            holder.btnAdd.isClickable = false
            holder.btnAdd.setBackgroundColor(Color.parseColor("#00C89D"))
            holder.btnAdd.setTextColor(Color.parseColor("#FFFFFF"))
        }
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.uca_image)
        val fullName: TextView = itemView.findViewById(R.id.uca_fullname)
        val mail: TextView = itemView.findViewById(R.id.uca_email)
        val btnAdd: MaterialButton = itemView.findViewById(R.id.event_btn)
    }
}