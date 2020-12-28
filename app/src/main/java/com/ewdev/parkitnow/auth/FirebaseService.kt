package com.ewdev.parkitnow.auth

import android.util.Log
import com.ewdev.parkitnow.data.CarQueue
import com.ewdev.parkitnow.data.ParkedCar
import com.ewdev.parkitnow.data.ParkedCar.Companion.toFirebaseFormat
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

    suspend fun getQueue(root: String): List<ParkedCar> {
        return db
            .collection("cars")
            .whereArrayContains("roots", root)
            .get()
            .await()
            .documents
            .map { doc -> doc.toParkedCar() }
    }

    /*
    More efficient than getQueue (doesn't require requesting two separate queues which may contain
    the same element)
     */
    suspend fun getQueues(roots: List<String>): List<ParkedCar> {
        return db
                .collection("cars")
                .whereArrayContainsAny("roots", roots)
                .get()
                .await()
                .documents
                .map { doc -> doc.toParkedCar() }
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

    suspend fun setCar(car: ParkedCar) {
        db
                .collection("cars")
                .document(car.licensePlate)
                .update(car.toFirebaseFormat())
                .await()
    }

    suspend fun updateQueue(queueId: String, carQueue: CarQueue) {
        db
                .collection("queues")
                .document(queueId)
                .update("carQueue", carQueue.toFirebaseFormat())
                .await()
    }

    suspend fun updateCarQueue(car: ParkedCar) {
        db
                .collection("cars")
//                .where
                .document(car.licensePlate)
//                .update("blockingQueue", car.queueId)
//                .await()
    }

}