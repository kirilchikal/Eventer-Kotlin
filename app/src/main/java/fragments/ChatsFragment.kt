package fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.eventer.R
import com.example.eventer.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_chats.*
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.button.MaterialButton

/**
 * A simple [Fragment] subclass.
 * Use the [ChatsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ChatsFragment : Fragment() {
    private lateinit var eventsList: RecyclerView
    private lateinit var invitedList: RecyclerView
    private var auth = FirebaseAuth.getInstance()
    private var dbrefEvents = Firebase.firestore.collection("events")
    private var dbref = Firebase.firestore.collection("users")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val infl = inflater.inflate(R.layout.fragment_chats, container, false)

        auth = FirebaseAuth.getInstance()

        eventsList = infl.findViewById(R.id.rv_events_list)
        eventsList.setHasFixedSize(true)
        eventsList.layoutManager = LinearLayoutManager(this.context)

        invitedList = infl.findViewById(R.id.rv_invited_list)
        invitedList.setHasFixedSize(true)
        invitedList.layoutManager = LinearLayoutManager(this.context)

        val data1 = ArrayList<Event>()
        val query = dbrefEvents
        query.get().addOnSuccessListener {
            for (doc in it!!) {
                if (doc.get("creator") == auth.uid)
                    data1.add(Event(doc.get("title").toString(), doc.get("date").toString(), doc.id))
            }
            if (data1.isEmpty())
                infl.findViewById<TextView>(R.id.tv_no_events).visibility = View.VISIBLE
            else
                infl.findViewById<TextView>(R.id.tv_no_events).visibility = View.GONE
        }.addOnCompleteListener {
            val adapter = EventsAdapter(data1)
            eventsList.adapter = adapter
        }

        val data2 = ArrayList<Event>()
        dbref.document(auth.uid!!).get().addOnSuccessListener {
            if (it.get("invited") != null) {
                infl.findViewById<TextView>(R.id.tv_no_invitations).visibility = View.GONE
                val invited: List<String> = it.get("invited") as List<String>      // list of invited events

                for (id in invited) {
                    dbrefEvents.document(id).get().addOnSuccessListener { doc ->
                        if (doc != null) {
                            var email = ""
                            dbref.document(doc.get("creator").toString()).get().addOnSuccessListener { email = it!!.get("email").toString() }
                                .addOnCompleteListener {
                                    data2.add(Event(doc.get("title").toString(), doc.get("date").toString().split(" ")[0], id, email))
                                }.addOnCompleteListener {
                                    val adapter = EventsAdapter(data2)
                                    invitedList.adapter = adapter
                                }
                        }
                    }
                }
            }
            else
                infl.findViewById<TextView>(R.id.tv_no_invitations).visibility = View.VISIBLE
        }

        return infl
    }



    class EventsAdapter(private val mList: List<Event>) :
        RecyclerView.Adapter<EventsAdapter.ViewHolderEvents>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderEvents {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.event_card, parent, false)
            return ViewHolderEvents(view)
        }

        override fun onBindViewHolder(holder: ViewHolderEvents, position: Int) {
            val events = mList[position]
            holder.title.text = events.title
            holder.date.text = events.date
            if (events.creator != null)
                holder.creator.text = "by ${events.creator}"
            holder.btnMore.setOnClickListener {
                val intent = Intent(holder.itemView.context, ShowEventActivity::class.java)
                intent.putExtra("id", events.id)
                holder.itemView.context.startActivity(intent)
            }
        }

        override fun getItemCount(): Int {
            return mList.size
        }

        // Holds the views for adding it to image and text
        class ViewHolderEvents(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val title: TextView = itemView.findViewById(R.id.event_title)
            val date: TextView = itemView.findViewById(R.id.event_date)
            val creator: TextView = itemView.findViewById(R.id.event_creator)
            val btnMore: MaterialButton = itemView.findViewById(R.id.event_btn)
        }
    }
}