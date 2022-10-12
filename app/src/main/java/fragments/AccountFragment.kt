package fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.eventer.*
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso



// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AccountFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AccountFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var navView: NavigationView
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_account, container, false)
        navView = rootView.findViewById(R.id.profile_menu)
        auth = FirebaseAuth.getInstance()
        database = Firebase.firestore

        val email = rootView.findViewById<TextView>(R.id.tv_email)
        val fullName = rootView.findViewById<TextView>(R.id.tv_fullname)
        val profileImg= rootView.findViewById<ImageView>(R.id.profile_image)

        val storageReference = FirebaseStorage.getInstance().reference

        val loading = LoadingDialog(this.activity)
        loading.startLoading()



        // Load profile photo from storage
        val pathRef = storageReference.child("users/" + auth.uid + "/profile.jpg")     //path users/userid/profile.jpg
        pathRef.downloadUrl.addOnSuccessListener {
            if (it != null) {
                Picasso.get().load(it).into(profileImg)
            }
        }.addOnFailureListener {
            profileImg.setBackgroundResource(R.drawable.prof_im)
        }.addOnCompleteListener {
            // Load email and full name from db
            val docRef = database.collection("users").document(auth.uid!!)
            docRef.get().addOnSuccessListener { document ->
                if (document != null) {
                    email.text = document.getString("email")
                    fullName.text = document.getString("fullname")
                }
            }.addOnCompleteListener {
                loading.isDismiss()
            }

        }


        navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_logout -> {
                    auth.signOut()
                    val intent = Intent(context, LoginActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    startActivity(intent)
                }
                R.id.nav_edit_profile -> {
                    val intent = Intent(context, EditProfile::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    intent.putExtra("email", email.text)
                    intent.putExtra("fullname", fullName.text)
                    intent.putExtra("uid", auth.uid)
                    startActivity(intent)
                }
                R.id.nav_change_password -> {
                    val intent = Intent(context, ChangePassword::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    startActivity(intent)
                }
                R.id.nav_find_friends -> {
                    val intent = Intent(context, FindFriends::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    startActivity(intent)
                }
                R.id.nav_friends -> {
                    val intent = Intent(context, MyFriends::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    startActivity(intent)
                }
            }
            true
        }

        return rootView
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AccountFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AccountFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}