package fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.eventer.R
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso

class MembersFragment : Fragment() {

    private lateinit var memberList: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val infl = inflater.inflate(R.layout.fragment_chat_members, container, false)

        memberList = infl.findViewById(R.id.rv_event_members)
        memberList.setHasFixedSize(true)
        memberList.layoutManager = LinearLayoutManager(this.context)

        val eventId = arguments?.getString("eventId")
        val data = ArrayList<Member>()
        val dbRef = Firebase.firestore.collection("users").whereArrayContains("invited", eventId as Any)
        val query = dbRef
        query.get().addOnSuccessListener {
            if (it != null) {
                for (doc in it!!) {
                    data.add(Member(doc.id, doc.get("fullname").toString(), doc.get("email").toString()))
                }
            }
        }.addOnCompleteListener {
            val adapter = MembersAdapter(data)
            memberList.adapter = adapter
        }
        return infl
    }

    data class Member(val uid: String, val fullname: String, val email: String) {}

    class MembersAdapter(private val mList: List<Member>) :
        RecyclerView.Adapter<MembersAdapter.ViewHolderEvents>() {

        private val storageReference = FirebaseStorage.getInstance().reference

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderEvents {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.event_member_card, parent, false)
            return ViewHolderEvents(view)
        }

        override fun onBindViewHolder(holder: ViewHolderEvents, position: Int) {
            val members = mList[position]

            val pathRef = storageReference.child("users/" + members.uid + "/profile.jpg")
            pathRef.downloadUrl.addOnSuccessListener {
                if (it != null) {
                    Picasso.get().load(it).into(holder.image)
                }
            }.addOnFailureListener {
                holder.image.setBackgroundResource(R.drawable.prof_im)
            }

            holder.fullname.text = members.fullname
            holder.email.text = members.email
        }

        override fun getItemCount(): Int {
            return mList.size
        }

        class ViewHolderEvents(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val image: ImageView = itemView.findViewById(R.id.emc_image)
            val fullname: TextView = itemView.findViewById(R.id.event_fullname)
            val email: TextView = itemView.findViewById(R.id.event_email)
        }
    }
}