package com.ewdev.parkitnow.auth

import com.ewdev.parkitnow.data.ParkedCar
import com.ewdev.parkitnow.data.ParkedCar.Companion.toParkedCars
import com.ewdev.parkitnow.data.User
import com.ewdev.parkitnow.data.User.Companion.toUser
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import java.lang.Exception
import kotlin.coroutines.suspendCoroutine

object FirebaseService {

    suspend fun getUser(userId: String): User? {
        val db = FirebaseFirestore.getInstance()
        return db.collection("users")
            .document(userId).get().await().toUser()
    }

    suspend fun getBlockedBy(queueId: String): List<ParkedCar> {
        val db = FirebaseFirestore.getInstance()
        return db.collection("blockingQueues")
            .document(queueId).get().await().toParkedCars()
    }

}