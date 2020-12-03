package com.ewdev.parkitnow.data

import android.os.Parcelable
import android.util.Log
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ParkedCar(
    val queueId: String,
    val licensePlate: String,
    val departureTime: String
) : Parcelable {

    companion object {
        fun DocumentSnapshot.toParkedCar(): ParkedCar? {
            // TODO: doesn't match DB format
            val licensePlate = getString("licensePlate")!!
            // TODO: make departureTime of type Time or sth
            val departureTime = getString("departureTime")!!
            // document id is uid
            return ParkedCar(id, licensePlate, departureTime)
        }

        fun DocumentSnapshot.toParkedCars(): List<ParkedCar> {
            val parkedCars = ArrayList<ParkedCar>()

            val queue: ArrayList<HashMap<String, String>> = get("blockingQueue") as ArrayList<HashMap<String, String>>
            queue.forEach { layer ->
                layer.forEach { (licensePlate, departureTime) ->
                    parkedCars.add(ParkedCar(id, licensePlate, departureTime))
                }
            }

            return parkedCars
        }
    }
}