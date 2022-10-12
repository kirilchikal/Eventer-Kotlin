package com.example.eventer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.firestore.ktx.firestore

class RegisterActivity : AppCompatActivity() {

    private lateinit var database: FirebaseFirestore

    private lateinit var auth: FirebaseAuth

    private lateinit var fullName: EditText
    private lateinit var emailEt: EditText
    private lateinit var passwordEt: EditText
    private lateinit var passwordRt: EditText

    private lateinit var signUpBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        auth = FirebaseAuth.getInstance()

        fullName = findViewById(R.id.full_name)
        emailEt= findViewById(R.id.email_et)
        passwordEt = findViewById(R.id.password_et)
        passwordRt = findViewById(R.id.retype_password)

        signUpBtn = findViewById(R.id.signUp)
        signUpBtn.setOnClickListener {
            var name: String = fullName.text.toString()
            var email: String = emailEt.text.toString()
            var password: String = passwordEt.text.toString()
            var password2: String = passwordRt.text.toString()

            if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(password2)) {
                Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show()
            }
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Please enter a valid email address", Toast.LENGTH_SHORT).show()
            }
            if (password != password2) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
            }
            else {
                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, OnCompleteListener{ task ->
                    if(task.isSuccessful){
                        Toast.makeText(this, "Successfully Registered", Toast.LENGTH_SHORT).show()
                        writeNewUser(auth.uid, name, email)  // AFTER CREATION NEW USER -> CREATE DOCUMENT IN USERS COLLECTION
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }else {
                        Toast.makeText(this, "Registration Failed", Toast.LENGTH_SHORT).show()
                    }
                })
            }
        }

        val signInTV = findViewById<TextView>(R.id.signInTextView)
        signInTV.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }


    private fun writeNewUser(userId: String?, name: String, mail: String) {
        database = Firebase.firestore
        val user = hashMapOf(
            "fullname" to name,
            "email" to mail
        )

        database.collection("users").document(userId!!)
            .set(user)
            .addOnSuccessListener { Toast.makeText(this, "Successfully written", Toast.LENGTH_LONG).show() }
            .addOnFailureListener { Toast.makeText(this, "Writing failed", Toast.LENGTH_LONG).show() }
    }
}