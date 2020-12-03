package com.ewdev.parkitnow.auth

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlin.coroutines.suspendCoroutine

class DbRepository {

    val firebaseUser = FirebaseAuth.getInstance().currentUser
    val db = Firebase.firestore

    init {
        if (firebaseUser == null) {
            // TOOD: shouldn't happen, but go to login
        }
    }

    suspend fun getStatus(): String = suspendCoroutine { continuation ->
        db.collection("users").document(firebaseUser!!.uid)
            .get()
    }

}