package com.ewdev.parkitnow.auth

import android.app.Application
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.ewdev.parkitnow.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_splash.*

class SplashFragment : Fragment() {

    // TODO: define SplashFragment as home in nav_graph.xml and use navController starting here
    lateinit var navController: NavController

    lateinit var authViewModel: AuthViewModel

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        authViewModel = ViewModelProvider(this).get(AuthViewModel::class.java)

//        FirebaseAuth.getInstance().signInWithEmailAndPassword("abcd_egfh@yahoo.com", "123456")
//            .addOnCompleteListener(authViewModel.getApplication<Application>().mainExecutor) {task ->
//                if (task.isSuccessful) {
////                    TODO("Logged in, go to next activity")
//
//                } else {
//                    task.exception?.message.let {
//                        Log.i("error", "did not log in")
//                    }
//                }
//            }


        authViewModel.userLiveData.observe(this, Observer { firebaseUser ->
            if (firebaseUser != null) {
//                    Navigation.findNavController(view!!)
//                        .navigate(R.id.action_loginRegisterFragment_to_loggedInFragment)
                Log.i("all good", "all good")
            }
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)  // navController has a reference to the navGraph

    }

}