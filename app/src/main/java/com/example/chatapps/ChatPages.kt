package com.example.chatapps
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chatapps.databinding.ActivityChatPagesBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ChatPages : AppCompatActivity() {
    lateinit var binding: ActivityChatPagesBinding
    private lateinit var massageAdaptor: MessageAdaptor
    private lateinit var messageList: ArrayList<Message>
    private lateinit var mDbRef: DatabaseReference

    var receviedRoom: String? = null
    var sandRoom: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatPagesBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val name = intent.getStringExtra("name")
        val receiveuid = intent.getStringExtra("uid")
        val senduid = FirebaseAuth.getInstance().currentUser?.uid

        mDbRef = FirebaseDatabase.getInstance().getReference()

        sandRoom = receiveuid + senduid
        receviedRoom = senduid + receiveuid

        supportActionBar?.title = name

        messageList = ArrayList()
        massageAdaptor = MessageAdaptor(this, messageList)


        binding.chatRecyclerview.layoutManager = LinearLayoutManager(this)
        binding.chatRecyclerview.adapter = massageAdaptor


        mDbRef.child("chat").child(sandRoom!!).child("messages")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    messageList.clear()
                    for (postSnapshort in snapshot.children) {
                        val message = postSnapshort.getValue(Message::class.java)
                        messageList.add(message!!)
                    }
                    massageAdaptor.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })




        binding.sentButton.setOnClickListener {
            val message = binding.messagebox.text.toString()
            val messageObject = Message(message, senduid)

            mDbRef.child("chat").child(sandRoom!!).child("messages").push()
                .setValue(messageObject).addOnSuccessListener {
                    mDbRef.child("chat").child(receviedRoom!!).child("messages").push()
                        .setValue(messageObject)
                }
            binding.messagebox.setText("")

        }

    }


}