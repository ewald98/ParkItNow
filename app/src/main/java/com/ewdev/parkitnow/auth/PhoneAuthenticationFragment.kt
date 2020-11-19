package com.ewdev.parkitnow.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.ewdev.parkitnow.R
import com.google.firebase.auth.FirebaseUser

class PhoneAuthenticationFragment : Fragment() {

    lateinit var authViewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        authViewModel = ViewModelProvider(this).get(AuthViewModel::class.java)

        authViewModel.userLiveData.observe(this, Observer { firebaseUser ->
                if (firebaseUser != null) {
//                    Navigation.findNavController(view!!)
//                        .navigate(R.id.action_loginRegisterFragment_to_loggedInFragment)
                }
            })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_phone_authentication, container, false)
    }

}