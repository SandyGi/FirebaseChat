package com.sandy.firebasechat.ui.activity

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.sandy.firebasechat.R
import kotlinx.android.synthetic.main.activity_registration.*
import java.util.*


class RegistrationActivity : AppCompatActivity() {

    private val mAuth: FirebaseAuth? = null
    private val TAG = RegistrationActivity::class.java.name
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)
        /**
         * Click on registration button
         */
        registrationButton.setOnClickListener {
            performRegister()
        }
        imgUser.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)
        }
    }

    private fun performRegister() {
        var email = itetRUserName.text.toString()
        var password = itetRPassword.text.toString()
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(
            email,
            password
        )
            .addOnCompleteListener {


                Log.e(TAG, "Email is $email}")
                Log.e(TAG, "Password is $password")

                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(this, "Please enter text email/password", Toast.LENGTH_SHORT)
                        .show()
                    return@addOnCompleteListener
                }
                if (!it.isSuccessful) return@addOnCompleteListener
                //else if successful
                Log.e(TAG, "Registration is Successful ${it.result!!.user!!.uid}")
                uploadProfileImage()
            }
            .addOnFailureListener {
                Log.e(TAG, "Failed the user creation : ${it.message}")
                Toast.makeText(this, "Failed the user creation : ${it.message}", Toast.LENGTH_SHORT)
                    .show()
            }

    }

    private fun uploadProfileImage() {
        if (selectedPhotoUri == null) return
        val fileName = UUID.randomUUID().toString()
        val path = FirebaseStorage.getInstance("gs://fir-chat-a29b0.appspot.com")
            .getReference("/profileImages/$fileName")
        path.putFile(selectedPhotoUri!!)
            .addOnSuccessListener {
                Log.e(TAG, "Image Successfully uploaded. ${it.metadata}")
                path.downloadUrl.addOnSuccessListener { url ->
                    Log.e(TAG, "Profile image url: $url")
                    saveCreatedUser(url.toString())
                }
            }

    }

    private fun saveCreatedUser(profileImageUrl: String) {
        val uuid = FirebaseAuth.getInstance().uid?:""
        val path = FirebaseDatabase.getInstance().getReference("/users/$uuid")
        val user = User(uuid, itetRUserName.text.toString(), profileImageUrl)
        path.setValue(user)
            .addOnSuccessListener {
                Log.e(TAG, "Save user details")
                val intent = Intent(this@RegistrationActivity, MessageActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
            .addOnFailureListener {
                Log.e(TAG, "User details not saved ${it.message}")
            }
    }

    var selectedPhotoUri: Uri? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            Log.e(TAG, "Photo was selected")
            selectedPhotoUri = data.data
            val imgBitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedPhotoUri)
            val bitmapDrawable = BitmapDrawable(imgBitmap)
            imgUser.setImageDrawable(bitmapDrawable)
        }
    }

    class User(val uid: String, val username: String, val profileImageUrl : String)
}
