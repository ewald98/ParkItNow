package com.ewdev.parkitnow.auth

import com.ewdev.parkitnow.data.CarQueue
import com.ewdev.parkitnow.data.CarQueue.Companion.toCarQueue
import com.ewdev.parkitnow.data.ParkedCar
import com.ewdev.parkitnow.data.ParkedCar.Companion.toParkedCar
import com.ewdev.parkitnow.data.User
import com.ewdev.parkitnow.data.User.Companion.toUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

object FirebaseService {

    val db = FirebaseFirestore.getInstance()

    suspend fun getUser(userId: String): User? {
        return db
            .collection("users")
            .document(userId)
            .get()
            .await()
            .toUser()
    }

    suspend fun getQueue(queueId: String): CarQueue {
        return db
            .collection("queues")
            .document(queueId)
            .get()
            .await()
            .toCarQueue()
    }

    suspend fun isValidCar(licensePlate: String): Boolean {
        return db
            .collection("cars")
            .document(licensePlate)
            .get()
            .await()
            .exists()
    }

    suspend fun getCar(licensePlate: String): ParkedCar? {
        val doc = db
            .collection("cars")
            .document(licensePlate)
            .get()
            .await()

        if (doc.exists())
            return doc.toParkedCar()
        else
            return null

    }

}