package com.sandy.firebasechat.ui.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.sandy.firebasechat.R
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private val TAG = LoginActivity::class.java.name
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        signInButton.setOnClickListener { doLogin() }
    }

    private fun doLogin() {

        FirebaseAuth.getInstance().signInWithEmailAndPassword(
            itetLUserName.text.toString(),
            itetLPassword.text.toString()
        )
            .addOnSuccessListener {
                Toast.makeText(this@LoginActivity, "Success login", Toast.LENGTH_SHORT).show()
                val intent = Intent(this@LoginActivity, LatestMessageActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
            .addOnFailureListener {
                Toast.makeText(this@LoginActivity, it.message, Toast.LENGTH_SHORT).show()
                Log.e(TAG, it.message)
            }
    }

}
