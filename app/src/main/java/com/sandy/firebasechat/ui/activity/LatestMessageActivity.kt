package com.sandy.firebasechat.ui.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.sandy.firebasechat.R
import com.sandy.firebasechat.model.MessageModel
import com.sandy.firebasechat.model.User
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.activity_latest_message.*
import kotlinx.android.synthetic.main.latest_message_row.view.*

class LatestMessageActivity : AppCompatActivity() {

    private val TAG = ChatLogActivity::class.java.name

    companion object{
        var currentUser : User? = null
    }
    val adapter = GroupAdapter<GroupieViewHolder>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_latest_message)

        rvLatestMessage.adapter = adapter
        ifUserAlreadyLogin()
        getCurrentUser()

        loadLatestMessages()
    }

    private fun loadLatestMessages() {
        val fromId = FirebaseAuth.getInstance().uid
        val latestToFromReference = FirebaseDatabase.getInstance().getReference("/latest-messages/$fromId")
        latestToFromReference.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val messageModel = p0.getValue(MessageModel::class.java)
                if (messageModel != null) {

                    adapter.add(LatestMessageItem(messageModel))
                    Log.e(TAG, messageModel.message)
                }
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {

            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {

            }

            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onChildRemoved(p0: DataSnapshot) {

            }
        })
//        val adapter = GroupAdapter<GroupieViewHolder>()
//        adapter.add(LatestMessageItem())
//        adapter.add(LatestMessageItem())
//        adapter.add(LatestMessageItem())
//        adapter.add(LatestMessageItem())
//        rvLatestMessage.adapter = adapter
    }

    /**
     * Get current user
     */
    private fun getCurrentUser() {
        val uid = FirebaseAuth.getInstance().uid
        val reference = FirebaseDatabase.getInstance().getReference("/users/$uid")
        reference.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                Log.e(TAG, p0.message)
            }

            override fun onDataChange(p0: DataSnapshot) {
                currentUser = p0.getValue(User::class.java)
            }
        })
    }

    class LatestMessageItem(val messageModel: MessageModel) : Item<GroupieViewHolder>(){
        override fun getLayout(): Int {
            return R.layout.latest_message_row
        }

        override fun bind(viewHolder: GroupieViewHolder, position: Int) {
            viewHolder.itemView.txtLatestUser.text = messageModel.toId
            viewHolder.itemView.txtLatestMsg.text = messageModel.message
        }
    }

    /**
     * Check user already login or not
     */
    private fun ifUserAlreadyLogin() {
        val uid = FirebaseAuth.getInstance().uid
        if (uid == null) {
            val intent = Intent(this@LatestMessageActivity, RegistrationActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            R.id.menu_new_message -> {
                val intent = Intent(this@LatestMessageActivity, NewMessageActivity::class.java)
                startActivity(intent)
//                finish()
            }
            R.id.menu_log_out -> {
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(this@LatestMessageActivity, RegistrationActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.nav_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }
}
