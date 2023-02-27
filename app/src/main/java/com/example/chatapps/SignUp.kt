package com.example.chatapps

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.chatapps.databinding.ActivitySignUpBinding
import com.google.firebase.auth.*
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SignUp : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var mDbRef:DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        auth = FirebaseAuth.getInstance()


        binding.signupButton.setOnClickListener {
            val name=binding.nameSignup.text.toString()
            val email=binding.emailSignup.text.toString().trim()
            val password=binding.passwordSignup.text.toString()

            signup(name,email,password)
        }


    }

    private fun signup(name:String, email:String, password:String ) {

        auth.createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener(this){ task ->
                if (task.isSuccessful){


                    addUserToDatabase(name,email,auth.currentUser?.uid!!)

                    val intent = Intent(this@SignUp,MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }else{
                    Toast.makeText(this@SignUp,"Problem in signing up",Toast.LENGTH_SHORT).show()
                }

            }
            .addOnFailureListener {
                Toast.makeText(this@SignUp,it.toString(),Toast.LENGTH_SHORT).show()
            }
    }

    private fun addUserToDatabase(name:String, email:String, uid:String?) {
        mDbRef=FirebaseDatabase.getInstance().getReference()
        mDbRef.child("User").child(uid!!).setValue(User(name,email,uid))

    }
}