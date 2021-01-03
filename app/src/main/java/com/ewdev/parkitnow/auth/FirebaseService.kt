package com.ewdev.parkitnow.auth

import android.util.Log
import com.ewdev.parkitnow.data.CarQueue
import com.ewdev.parkitnow.data.ParkedCar
import com.ewdev.parkitnow.data.ParkedCar.Companion.toFirebaseFormat
import com.ewdev.parkitnow.data.ParkedCar.Companion.toParkedCar
import com.ewdev.parkitnow.data.User
import com.ewdev.parkitnow.data.User.Companion.toFirebaseFormat
import com.ewdev.parkitnow.data.User.Companion.toUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.lang.Exception

object FirebaseService {

    val db = FirebaseFirestore.getInstance()

    suspend fun getUser(userId: String): User? {
        val doc = db
            .collection("users")
            .document(userId)
            .get()
            .await()

        if (doc.exists())
            return doc.toUser()
        else
            return null
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

    suspend fun addNewUser(uid: String, phoneNo: String): Boolean {
        try {
            db
                    .collection("users")
                    .document(uid)
                    .set(
                            User(
                                    uid,
                                    phoneNo,
                                    null,
                                    false
                            ).toFirebaseFormat()
                    )
                    .await()
            return true
        } catch (e: Exception) {
            return false
        }

    }

}