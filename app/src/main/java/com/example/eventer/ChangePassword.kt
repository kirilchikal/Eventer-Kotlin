package com.example.eventer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_change_password.*

class ChangePassword : AppCompatActivity() {

    private lateinit var tool_bar: androidx.appcompat.widget.Toolbar
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)

        tool_bar = findViewById(R.id.toolbar_change_password)

        tool_bar.setNavigationIcon(R.drawable.ic_back)
        tool_bar.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("activity", 2)
            startActivity(intent)
        }

        auth = FirebaseAuth.getInstance()
        btn_change_password.setOnClickListener {
            changePassword()
        }
    }

    private fun changePassword() {
        if (et_current_password.text!!.isNotEmpty() &&
            et_new_password.text!!.isNotEmpty() &&
            et_confirm_password.text!!.isNotEmpty()) {
            if (et_new_password.text.toString().equals(et_confirm_password.text.toString())) {
                val user = auth.currentUser
                if (user != null && user.email != null) {
                    val credential = EmailAuthProvider
                        .getCredential(user.email!!, et_current_password.text.toString())

                    user?.reauthenticate(credential)
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                                user?.updatePassword(et_new_password.text.toString())
                                    .addOnCompleteListener {
                                        if (it.isSuccessful) {
                                            Toast.makeText(this,"Password changed",Toast.LENGTH_SHORT).show()
                                            Handler().postDelayed({
                                                val intent = Intent(this, MainActivity::class.java)
                                                intent.putExtra("activity", 2)
                                                startActivity(intent)}, 1000)
                                        }
                                    }
                            }
                        }
                }
            }
            else {
                Toast.makeText(this, "Password mismatching", Toast.LENGTH_SHORT).show()
            }
        }
        else {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
        }
    }
}