package com.sandy.firebasechat.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.firebase.client.Firebase
import com.sandy.firebasechat.R
import com.sandy.firebasechat.presenter.loginpresenter.FirebaseLoginPresenter
import com.sandy.firebasechat.ui.view.LoginView
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity(), LoginView, View.OnClickListener {

    private var fbPresenter: FirebaseLoginPresenter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        init()
    }

    fun init() {
        Firebase.setAndroidContext(this)
        fbPresenter = FirebaseLoginPresenter(this)
    }

    override fun onClick(v: View?) {
        fbPresenter!!.receiveUserLogin(itetUserName.text.toString(), itetPassword.text.toString())
    }

    override fun logTheUserIn(username: String, uid: String, emoji: String) {
        val i = Intent(this, MainActivity::class.java)
        i.putExtra("uid", uid) //uid not key
        i.putExtra("username", username)
        i.putExtra("emoji", emoji)
        startActivity(i)
    }

    override fun onFailure() {
        Toast.makeText(this, R.string.on_failure_message, Toast.LENGTH_SHORT).show();
    }

    override fun spinProgressBar() {
        progressBar.visibility = View.VISIBLE
    }

    override fun stopProgressBar() {
        progressBar.visibility = View.GONE
    }

}
