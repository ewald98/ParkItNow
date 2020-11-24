package com.ewdev.parkitnow.auth

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.ewdev.parkitnow.R
import com.google.firebase.auth.FirebaseAuth

class SplashFragment : Fragment() {

    // TODO: define SplashFragment as home in nav_graph.xml and use navController starting here
    lateinit var navController: NavController

    lateinit var authViewModel: AuthViewModel

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        authViewModel = ViewModelProvider(this).get(AuthViewModel::class.java)

        authViewModel.isLoggedInLiveData.observe(this, Observer { userIsLogged ->
            Log.i("userIsLogged", userIsLogged.toString())
            if (userIsLogged) {
                Navigation.findNavController(requireView())
                        .navigate(R.id.action_splashFragment_to_homeFragment)
                Log.i("nav_action", "completed: action_splashFragment_to_homeFragment")
            } else {
                Navigation.findNavController(requireView())
                        .navigate(R.id.action_splashFragment_to_phoneAuthenticationFragment)
                Log.i("nav_action", "completed: action_splashFragment_to_phoneAuthenticationFragment")
            }
        })



//        this.activity?.mainExecutor?.let {
//            FirebaseAuth.getInstance().signInWithEmailAndPassword("abcd_egfh@yahoo.com", "123456")
//                    .addOnCompleteListener(it) { task ->
//                        if (task.isSuccessful) {
////                    TODO("Logged in, go to next activity")
//
//                            if (FirebaseAuth.getInstance().currentUser == null) {
//                                Log.i("error", "no one logged in")
//                            }
//                            authViewModel.doIt()
//
//
//                        } else {
//                            task.exception?.message.let {
//                                Log.i("error", "did not log in")
//                            }
//                        }
//                    }
//        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        navController = Navigation.findNavController(view)  // navController has a reference to the navGraph

    }

}