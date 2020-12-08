package com.ewdev.parkitnow.view.fragments

import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.ewdev.parkitnow.R
import com.ewdev.parkitnow.viewModel.AuthViewModel

class SplashFragment : Fragment() {

    // TODO: add countdown timer and resend message
    lateinit var authViewModel: AuthViewModel

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        authViewModel = ViewModelProvider(this).get(AuthViewModel::class.java)

        authViewModel.isLoggedIn.observe(this, Observer { isLoggedIn ->
            Log.i("userIsLogged", isLoggedIn.toString())
            if (isLoggedIn) {
                authViewModel.requestIsParked()
                authViewModel.isParked.observe(viewLifecycleOwner, Observer { isParked ->
                    if (isParked) {
                        findNavController()
                                .navigate(R.id.action_splashFragment_to_homeParkedFragment)
                        Log.i("nav_action", "completed: action_splashFragment_to_homeParkedFragment")
                    } else {
                        findNavController()
                                .navigate(R.id.action_splashFragment_to_homeUnparkedFragment)
                        Log.i("nav_action", "completed: action_splashFragment_to_homeUnparkedFragment")
                    }
                })
            } else {
                findNavController()
                    .navigate(R.id.action_splashFragment_to_phoneAuthenticationFragment)
                Log.i("nav_action", "completed: action_splashFragment_to_phoneAuthenticationFragment")
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

        // wait for some time and display splash
        Handler(Looper.getMainLooper()).postDelayed({
            authViewModel.requestIsLoggedIn()
        }, 2000)
//        navController = Navigation.findNavController(view)  // navController has a reference to the navGraph
    }

}