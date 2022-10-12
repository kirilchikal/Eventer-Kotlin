package com.example.eventer

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import org.json.JSONArray
import java.util.*
import kotlin.collections.ArrayList

class CreateEvent : AppCompatActivity(), DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    var day = 0
    var month = 0
    var year = 0
    var hour = 0
    var minute = 0

    var savedDay = 0
    var savedMonth = 0
    var savedYear = 0
    var savedHour = 0
    var savedMinute = 0

    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseFirestore

    private lateinit var title: EditText
    private lateinit var place: EditText
    private lateinit var date: EditText
    private lateinit var time: EditText

    private lateinit var createBtn: Button

    private lateinit var toolBar: Toolbar

    var membersList = arrayListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_event)

        toolBar = findViewById(R.id.toolbar_create_event)
        //setSupportActionBar(tool_bar)
        toolBar.setNavigationIcon(R.drawable.ic_close)
        toolBar.setNavigationOnClickListener  {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        title = findViewById(R.id.title)
        place = findViewById(R.id.place)
        date = findViewById(R.id.date)

        val myCalendar = Calendar.getInstance()
        val datePicker = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            myCalendar.set(Calendar.YEAR,year)
            myCalendar.set(Calendar.MONTH,month)
            myCalendar.set(Calendar.DAY_OF_MONTH,dayOfMonth)
        }

        date.setOnClickListener{
            getDateTimeCalendar()

            DatePickerDialog(this,this, year, month,day).show()
        }

        createBtn = findViewById(R.id.create)
        createBtn.setOnClickListener {
            var titleW: String = title.text.toString()
            var placeW: String = place.text.toString() // Timestamp(year: Int, month: Int, date: Int, hour: Int, minute: Int, second: Int, nano: Int)
            var dateW: String = date.text.toString()
            if (TextUtils.isEmpty(titleW) || TextUtils.isEmpty(placeW) || TextUtils.isEmpty(dateW.toString())) {
                Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show()
            } else if (membersList.isEmpty()) {
                Toast.makeText(this, "Please add  members", Toast.LENGTH_SHORT).show()
            }
            else {
                // create new event in db
                writeNewEvent(titleW, placeW, dateW, membersList)
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        findViewById<Button>(R.id.addMembers).setOnClickListener{
            val intent = Intent(this, AddMembers::class.java)
            startActivityForResult(intent, 1)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                membersList = data?.getStringArrayListExtra("members")!!
                if (membersList.isNotEmpty()) {
                    findViewById<TextView>(R.id.n_members_add).text =
                        membersList.size.toString() + " members selected"
                }
            }
        }
    }


    private fun writeNewEvent(title: String, place: String, date: String, members: ArrayList<String>) {
        database = Firebase.firestore
        auth = FirebaseAuth.getInstance()
        val event = hashMapOf(
            "creator" to auth.uid,
            "title" to title,
            "place" to place,
            "date" to date,
            "members" to members
        )

        /*
        database.collection("users").document(auth.uid!!)
                .update("friends", FieldValue.arrayUnion(searchFriends.uid))
        */
        var uniqueID = UUID.randomUUID().toString()
        database.collection("events").document(uniqueID).set(event)
                .addOnSuccessListener { Toast.makeText(this, "Successfully written", Toast.LENGTH_SHORT).show() }
                .addOnCompleteListener {
                    database.collection("users").document(auth.uid!!)
                        .update("myevents", FieldValue.arrayUnion(uniqueID))

                    for (member in members) {
                        database.collection("users").document(member)       //for every member add event id to invited field
                            .update("invited", FieldValue.arrayUnion(uniqueID))
                    }
                }
                .addOnFailureListener { Toast.makeText(this, "Writing failed", Toast.LENGTH_SHORT).show() }

    }

    private fun getDateTimeCalendar(){
        val cal = Calendar.getInstance()
        day = cal.get(Calendar.DAY_OF_MONTH)
        month = cal.get(Calendar.MONTH)
        year = cal.get(Calendar.YEAR)
        hour = cal.get(Calendar.HOUR)
        minute = cal.get(Calendar.MINUTE)
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        savedDay = dayOfMonth
        savedMonth = month
        savedYear = year

        getDateTimeCalendar()
        TimePickerDialog(this,this,hour, minute, true).show()
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        savedHour = hourOfDay
        savedMinute = minute

        date.setText( "$savedDay/$savedMonth/$savedYear  $savedHour:$savedMinute")
    }
}