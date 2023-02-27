package com.example.chatapps

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.chatapps.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth


class Login : AppCompatActivity() {

    private lateinit var binding:ActivityLoginBinding
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        auth=FirebaseAuth.getInstance()


        binding.signupButton.setOnClickListener {
            val intent = Intent(this, SignUp::class.java)
            startActivity(intent)
        }

        binding.loginButton.setOnClickListener {
                val email = binding.emailLogin.text.toString().trim()
                val password = binding.passwordLogin.text.toString()
                login(email,password)
        }
    }

    private fun login(email: String, password: String) {
        auth.signInWithEmailAndPassword(email,password)
            .addOnCompleteListener(this){ task->
                if (task.isSuccessful){
                     val intent = Intent(this,MainActivity::class.java)
                    startActivity(intent)
                    finish()

                }else{

                    Toast.makeText(this,"user dose not exist",Toast.LENGTH_SHORT).show()
                }

            }

    }


}
