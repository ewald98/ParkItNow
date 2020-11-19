package com.ewdev.parkitnow.old

import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import com.ewdev.parkitnow.R
import com.google.firebase.auth.FirebaseAuth

class LoginFragment1 {

    private lateinit var mAuth: FirebaseAuth

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.fragment_login)
//
//        mAuth = FirebaseAuth.getInstance()
//
//        button_sign_in.setOnClickListener {
//            val email = edit_text_email.text.toString().trim()
//            val password = edit_text_password.text.toString().trim()
//
//            if(email.isEmpty()) {
//                edit_text_email.error = "Email unspecified"
//                edit_text_email.requestFocus()
//                return@setOnClickListener
//            }
//            if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
//                edit_text_email.error = "Invalid Email specified"
//                edit_text_email.requestFocus()
//                return@setOnClickListener
//            }
//
//            if(password.isEmpty() || password.length < 8){
//                edit_text_password.error = "8 character password required"
//                edit_text_password.requestFocus()
//                return@setOnClickListener
//            }
//
//            loginUser(email, password)
//        }
//
//        text_view_register.setOnClickListener {
//            TODO("Unimplemented register activity")
//        }
//
//        text_view_forget_password.setOnClickListener {
//            TODO ("Unimplemented forgot password activity")
//        }
//    }
//
//    private fun loginUser(email: String, password: String) {
//        progressbar.visibility = View.VISIBLE
//        mAuth.signInWithEmailAndPassword(email, password)
//                .addOnCompleteListener(this) {task ->
//                    progressbar.visibility = View.GONE
//                    if (task.isSuccessful) {
//                        TODO("Logged in, go to next activity")
//                    } else {
//                        task.exception?.message.let {
//                            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
//                        }
//                    }
//                }
//    }
//
//    override fun onStart() {
//        super.onStart()
//
//        mAuth.currentUser?.let {
//            TODO("Logged in, go to next activity")
//        }
//    }
//
}