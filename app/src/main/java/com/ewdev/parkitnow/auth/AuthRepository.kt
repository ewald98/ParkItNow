package com.ewdev.parkitnow.auth

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class AuthRepository {

    val firebaseUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser
    val userLiveData: MutableLiveData<FirebaseUser> = MutableLiveData()

    init {
        if (firebaseUser != null)
            userLiveData.postValue(firebaseUser)

    }

    // doesn't need to be suspend, no blocking io operation is taking place...
    fun geUserUid(): String? {
        if (firebaseUser != null) {
            return firebaseUser.uid
        }
        return null
    }

}