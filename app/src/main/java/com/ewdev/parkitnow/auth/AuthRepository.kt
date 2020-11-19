package com.ewdev.parkitnow.auth

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class AuthRepository(application: Application) {

    lateinit var application: Application
    lateinit var firebaseAuth: FirebaseAuth

    lateinit var userLiveData: MutableLiveData<FirebaseUser>

    init {
        firebaseAuth = FirebaseAuth.getInstance()
        userLiveData = MutableLiveData()

        val user = firebaseAuth.currentUser

        if (user != null)
            userLiveData.postValue(user)
    }


}