package com.example.eventer

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso

class AddMembers : AppCompatActivity() {

    private lateinit var toolBar: Toolbar
    private lateinit var friendsList: RecyclerView
    private var dbref = Firebase.firestore.collection("users")
    private var auth = FirebaseAuth.getInstance()
    private lateinit var adapter: MembersAdapter
    private val data = ArrayList<Friends>()


    inner class Friends(val uid: String, val mail: String, val fullName: String) {
        var isSelected: Boolean = false
    }

    internal class MembersAdapter(private val mList: List<Friends>) : RecyclerView.Adapter<MembersAdapter.ViewHolderMembers>() {
        var checkedFriends = ArrayList<Friends>()
        private val storageReference = FirebaseStorage.getInstance().reference

        // create new views
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderMembers {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.member_card_add, parent, false)

            return ViewHolderMembers(view)
        }

        // binds the list items to a view
        override fun onBindViewHolder(holder: ViewHolderMembers, @SuppressLint("RecyclerView") position: Int) {
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
            holder.checkBox.isChecked = searchFriends.isSelected

            holder.setItemClickListener(object : ViewHolderMembers.ItemClickListener {
                override fun onItemClick(v: View, pos: Int) {
                    val myCheckBox = v as CheckBox
                    val currentFriend = mList[position]

                    if (myCheckBox.isChecked) {
                        currentFriend.isSelected = true
                        checkedFriends.add(currentFriend)
                    } else if (!myCheckBox.isChecked) {
                        currentFriend.isSelected = false
                        checkedFriends.remove(currentFriend)
                    }
                }
            })
        }

        override fun getItemCount(): Int {
            return mList.size
        }

        // Holds the views for adding it to image and text
        internal class ViewHolderMembers(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
            var image: ImageView
            var fullName: TextView
            var mail: TextView
            var checkBox: CheckBox
            lateinit var myItemClickListener: ItemClickListener

            init {
                image = itemView.findViewById(R.id.mca_image)
                fullName = itemView.findViewById(R.id.mca_fullname)
                mail = itemView.findViewById(R.id.mca_email)
                checkBox = itemView.findViewById(R.id.mca_check_box)
                checkBox.setOnClickListener(this)
            }

            fun setItemClickListener(ic: ItemClickListener) {
                this.myItemClickListener = ic
            }

            override fun onClick(v: View) {
                this.myItemClickListener.onItemClick(v, layoutPosition)
            }

            internal interface ItemClickListener {
                fun onItemClick(v: View, pos: Int)
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_members)

        toolBar = findViewById(R.id.toolbar_add_members)
        friendsList = findViewById(R.id.rv_members_list)

        friendsList.setHasFixedSize(true)
        friendsList.layoutManager = LinearLayoutManager(this)

        dbref.document(auth.uid!!).get().addOnSuccessListener {
            if (it.get("friends") != null) {
                val friends: List<String> = it.get("friends") as List<String>
                for (id in friends) {
                    dbref.document(id).get().addOnSuccessListener { doc ->
                        if (doc != null) {
                            data.add(Friends(id, doc.get("email").toString(),doc.get("fullname").toString()))
                        }
                    }.addOnCompleteListener {
                        adapter = MembersAdapter(data)
                        friendsList.adapter = adapter
                    }
                }
            }
        }

        toolBar.setNavigationIcon(R.drawable.ic_back)
        toolBar.setNavigationOnClickListener  {
            val intentResult = Intent()
            val checkedFriends = getCheckedFriends()
            // send selected members back to event create activity
            intentResult.putExtra("members", checkedFriends)
            setResult(RESULT_OK, intentResult)
            finish()
        }
    }

    private fun getCheckedFriends(): ArrayList<String> {
        val list = arrayListOf<String>()
        if (adapter.checkedFriends.size == 0) {
            return list
        }

        for (member in adapter.checkedFriends) {
            list.add(member.uid)
        }
        return list
    }

}


