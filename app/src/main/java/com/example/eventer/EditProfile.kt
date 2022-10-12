package com.example.eventer

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.widget.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_edit_profile.*

class EditProfile : AppCompatActivity() {
    private lateinit var tool_bar: androidx.appcompat.widget.Toolbar
    private lateinit var iv_old_image: ImageView
    private lateinit var upload_img: TextView
    private lateinit var storageReference: StorageReference
    private var imageUri: Uri? = null
    private lateinit var userid: String
    private var isNewImageSet = false
    private lateinit var et_old_email: EditText
    private lateinit var et_old_fullname: EditText
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        iv_old_image = findViewById(R.id.old_image)
        upload_img = findViewById(R.id.upload_image)
        et_old_email = findViewById(R.id.edit_email)
        et_old_fullname = findViewById(R.id.edit_fullname)
        storageReference = FirebaseStorage.getInstance().reference
        tool_bar = findViewById(R.id.toolbar_edit_profile)
        auth = FirebaseAuth.getInstance()

        tool_bar.setNavigationIcon(R.drawable.ic_back)
        tool_bar.setNavigationOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("activity", 2)
            startActivity(intent)
        }

        // Recieve email, fullname and uid from parent activity
        val old_email = intent.getStringExtra("email")
        val old_fullname = intent.getStringExtra("fullname")
        userid = intent.getStringExtra("uid").toString()
        // Add old email and full name into editText
        edit_email.setHint(old_email)
        edit_fullname.setHint(old_fullname)


        // Load old image from storage
        val pathRef = storageReference.child("users/" + userid + "/profile.jpg")
        pathRef.downloadUrl.addOnSuccessListener {
            if (it != null) {
                Picasso.get().load(it).into(iv_old_image)
            }
        }.addOnFailureListener {
            iv_old_image.setBackgroundResource(R.drawable.prof_im)
        }

        // Open gallery
        upload_img.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, 1000)
        }


        val btn = findViewById<Button>(R.id.btn_save_changes)   // When SAVE CHANGES clicked
        btn.setOnClickListener {
            var email_valid = true
            var success = false
            val new_email = et_old_email.text.toString()
            val new_fullname = et_old_fullname.text.toString()

            if (!new_email.isNullOrEmpty()) {                   // Check email validity if set
                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(new_email).matches()) {
                    email_valid = false
                    Toast.makeText(this, "Invalid email address", Toast.LENGTH_SHORT).show()
                }
            }

            if (email_valid) success = updateUserData(new_email, new_fullname)

            if (success) {
                Toast.makeText(this, "Changes successfully saved!", Toast.LENGTH_SHORT).show()
                if (isNewImageSet) uploadNewImage()
                Handler().postDelayed({
                    val intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("activity", 2)
                    startActivity(intent)}, 1200)
            }
        }
    }

    private fun updateUserData(newEmail: String, newFullname: String): Boolean {
        val database = Firebase.firestore

        if (!newEmail.isNullOrEmpty()) {
            auth.currentUser!!.updateEmail(newEmail)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful)
                    {
                        database.collection("users").document(auth.uid!!).update("email", newEmail)
                    }
                    else {
                        Toast.makeText(this, "Failure", Toast.LENGTH_SHORT).show()
                    }
                }
        }
        if (!newFullname.isNullOrEmpty()) {
            database.collection("users").document(auth.uid!!).update("fullname", newFullname)
        }
        return true
    }

    private fun uploadNewImage() {
        // Upload a new image to firebase storage
        val fileRef = storageReference.child("users/" + userid + "/profile.jpg")
        fileRef.putFile(imageUri!!).addOnSuccessListener {
            // Uploaded
        }.addOnFailureListener {
            Toast.makeText(this, "faild upload image", Toast.LENGTH_SHORT).show()
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1000 && resultCode == RESULT_OK) {
            imageUri = data?.data
            iv_old_image.setImageURI(imageUri)
            isNewImageSet = true
        }
    }


}