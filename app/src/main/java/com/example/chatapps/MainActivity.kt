package com.example.chatapps

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.chatapps.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class MainActivity : AppCompatActivity() {

    private lateinit var userRecyclerView: RecyclerView
    private lateinit var userList: ArrayList<User>
    private lateinit var adaptor: UserAdaptor
    private lateinit var auth: FirebaseAuth
    private lateinit var mDbRef:DatabaseReference

    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        auth = FirebaseAuth.getInstance()
        mDbRef=FirebaseDatabase.getInstance().getReference()
        userList = ArrayList()
        adaptor=UserAdaptor(this,userList)
        userRecyclerView=binding.userRecyclerView
        userRecyclerView.layoutManager=LinearLayoutManager(this)
        userRecyclerView.adapter=adaptor


        mDbRef.child("User")
            .addValueEventListener(object :ValueEventListener{

            override fun onDataChange(snapshot: DataSnapshot) {

                userList.clear()
                for (postSnapshot in snapshot.children){
                    val currentUser=postSnapshot.getValue(User::class.java)

                    if (auth.currentUser?.uid != currentUser?.uid){
                        userList.add(currentUser!!)
                    }

                }
                adaptor.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.manu, menu)
        return super.onCreateOptionsMenu(menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.logout){
            auth.signOut()
            val intent = Intent(this,Login::class.java)
            startActivity(intent)
            finish()
            return true

        }
        return true
    }

}