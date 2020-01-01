package com.sandy.firebasechat.ui.activity

import BaseAdapter
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.sandy.firebasechat.utils.Constants.USER_KEY
import com.sandy.firebasechat.R
import com.sandy.firebasechat.model.User
import kotlinx.android.synthetic.main.activity_new_message.*
import kotlinx.android.synthetic.main.list_user.view.*

class NewMessageActivity : AppCompatActivity() {
    private val TAG = NewMessageActivity::class.java.name
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_message)

        supportActionBar?.title = "Select User"
        rv_new_message.adapter
        fetchAllUserList()
    }

    /**
     * Get all registered user list
     */
    private fun fetchAllUserList() {
        val ref = FirebaseDatabase.getInstance().getReference("/users")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(databaseError: DatabaseError) {

            }

            override fun onDataChange(dataSnapShot: DataSnapshot) {

               val userList =  ArrayList<User>()
                dataSnapShot.children.forEach {
                    Log.e(TAG, it.toString())
                    val user = it.getValue(User::class.java)
                    userList.add(user!!)
                }
                setUsers(userList)
            }
        })
    }

    /**
     * set user list
     */
    private fun setUsers(orderList: List<User>) {
        with(findViewById<RecyclerView>(R.id.rv_new_message)) {
            itemAnimator = DefaultItemAnimator()

            adapter = object : BaseAdapter<User, View>(
                this@NewMessageActivity,
                orderList as ArrayList<User>
            ) {
                override val layoutResId: Int
                    get() = R.layout.list_user

                override fun onBindData(model: User, position: Int, dataBinding: View) {
                    dataBinding.txtUserName.text = model.username
                    Glide
                        .with(this@NewMessageActivity)
                        .load(model.profileImageUrl)
                        .centerCrop()
                        .placeholder(R.drawable.default_profile)
                        .into(dataBinding.civ_imgUser);
                }

                override fun onItemClick(model: User, position: Int) {
                    val intent = Intent(this@NewMessageActivity, ChatLogActivity::class.java)
                    intent.putExtra(USER_KEY, model)
                    startActivity(intent)
                }
            }
        }
    }
}
