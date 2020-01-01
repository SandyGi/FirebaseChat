package com.sandy.firebasechat.ui.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.sandy.firebasechat.utils.Constants.USER_KEY
import com.sandy.firebasechat.R
import com.sandy.firebasechat.model.MessageModel
import com.sandy.firebasechat.model.User
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.activity_chat_log.*
import kotlinx.android.synthetic.main.chat_from_row.view.*
import kotlinx.android.synthetic.main.chat_to_row.view.*

class ChatLogActivity : AppCompatActivity() {
    private val TAG = ChatLogActivity::class.java.name
    private lateinit var user: User
    private var adapter = GroupAdapter<GroupieViewHolder>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_log)
        user = intent.getParcelableExtra<User>(USER_KEY)
        supportActionBar!!.title = user.fullName
        rvChatLog.adapter = adapter

        btnSendMsg.setOnClickListener {
            sendMsgToFirebase()
        }
        loadMessagesFromFiresae()
    }

    /**
     * Load message from the firebase
     */
    private fun loadMessagesFromFiresae() {
//        val reference = FirebaseDatabase.getInstance().getReference("/messages")
        val reference = FirebaseDatabase.getInstance().getReference("/user-messages/${FirebaseAuth.getInstance().uid}/${user.uid}")
        reference.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val messageModel = p0.getValue(MessageModel::class.java)
                if (messageModel != null) {
                    if (messageModel.fromId == FirebaseAuth.getInstance().uid){
                        if (LatestMessageActivity.currentUser == null) return
                        adapter.add(ChatFromItems(messageModel.message, LatestMessageActivity.currentUser!!))
                    }else{
                        adapter.add(ChatToItems(messageModel.message, user))
                    }

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
    }

    /**
     *Store message in the firebase database
     */
    private fun sendMsgToFirebase() {
//        val reference = FirebaseDatabase.getInstance().getReference("/messages").push()
        val fromId = FirebaseAuth.getInstance().uid
        val toId = user.uid
        if (fromId == null) return

        val fromReference = FirebaseDatabase.getInstance().getReference("/user-messages/$fromId/$toId").push()
        val fromMessage = MessageModel(
            fromReference.key!!,
            edtMsg.text.toString(),
            fromId,
            toId,
            System.currentTimeMillis() / 1000
        )
        fromReference.setValue(fromMessage)
            .addOnSuccessListener {
                Log.e(TAG, "Save message on firebase${fromReference.key}")
                edtMsg.text.clear()
                rvChatLog.scrollToPosition(adapter.itemCount -1)
            }
        val toReference = FirebaseDatabase.getInstance().getReference("/user-messages/$toId/$fromId").push()

        toReference.setValue(fromMessage)

        val latestFromToReference = FirebaseDatabase.getInstance().getReference("/latest-messages/$fromId/$toId").push()

        latestFromToReference.setValue(fromMessage)

        val latestToFromReference = FirebaseDatabase.getInstance().getReference("/latest-messages/$toId/$fromId").push()

        latestToFromReference.setValue(fromMessage)
    }


    /**
     * Create from user model
     */
    class ChatFromItems(val fromMsg: String, val user : User) : Item<GroupieViewHolder>() {
        override fun getLayout(): Int {
            return R.layout.chat_from_row;
        }

        override fun bind(viewHolder: GroupieViewHolder, position: Int) {
            viewHolder.itemView.txtFromMsg.text = fromMsg
            Glide.with(viewHolder.itemView.context)
                .load(user.profileImageUrl)
                .centerCrop()
                .placeholder(R.drawable.default_profile)
                .into(viewHolder.itemView.ivFromUser);

        }
    }

    /**
     * Create to user model
     */
    class ChatToItems(val toMsg: String, val user : User) : Item<GroupieViewHolder>() {
        override fun getLayout(): Int {
            return R.layout.chat_to_row;
        }

        override fun bind(viewHolder: GroupieViewHolder, position: Int) {
            viewHolder.itemView.txtToMsg.text = toMsg

            Glide
                .with(viewHolder.itemView.context)
                .load(user.profileImageUrl)
                .centerCrop()
                .placeholder(R.drawable.default_profile)
                .into(viewHolder.itemView.ivToUser);
        }
    }
}
