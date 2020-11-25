package com.ewdev.parkitnow.auth

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.lang.Thread.sleep

class AuthRepository {

    val firebaseUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser
    val userLiveData: MutableLiveData<FirebaseUser> = MutableLiveData()

    init {
        if (firebaseUser != null)
            userLiveData.postValue(firebaseUser)

    }



}