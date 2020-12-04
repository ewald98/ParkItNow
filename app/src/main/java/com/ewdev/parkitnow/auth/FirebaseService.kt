package com.ewdev.parkitnow.auth

import com.ewdev.parkitnow.data.CarQueue
import com.ewdev.parkitnow.data.CarQueue.Companion.toCarQueue
import com.ewdev.parkitnow.data.ParkedCar
import com.ewdev.parkitnow.data.User
import com.ewdev.parkitnow.data.User.Companion.toUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

object FirebaseService {

    suspend fun getUser(userId: String): User? {
        val db = FirebaseFirestore.getInstance()
        return db.collection("users")
            .document(userId).get().await().toUser()
    }

    suspend fun getCarQueue(queueId: String): CarQueue {
        val db = FirebaseFirestore.getInstance()
        return db.collection("queues")
            .document(queueId).get().await().toCarQueue()
    }

}