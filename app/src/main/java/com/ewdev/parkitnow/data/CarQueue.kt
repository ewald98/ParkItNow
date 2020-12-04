package com.ewdev.parkitnow.data

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class CarQueue(val carQueue: ArrayList<ArrayList<ParkedCar>>) {

    fun getBlockedCars(carLicensePlate: String): ArrayList<RelativeParkedCar> {
        val blockedCars = ArrayList<ParkedCar>()

        for ((i, layer) in carQueue.withIndex()) {
            if (isInLayer(carLicensePlate, layer)) {
                break;
            } else {
                layer.forEach { car ->
                    blockedCars.add(car)
                }
            }
        }

        val now = Date()
        val blockedCars2: ArrayList<RelativeParkedCar> = blockedCars.map { car ->
            RelativeParkedCar(
                    car.licensePlate,
                    (car.departureTime.time - Date().time).toString()
            )
        } as ArrayList<RelativeParkedCar>

        return blockedCars2
    }

    fun isInLayer(carLicensePlate: String, layer: ArrayList<ParkedCar>): Boolean {
        return layer.any { parkedCar -> carLicensePlate.equals(parkedCar.licensePlate) }
    }

    companion object {
        fun DocumentSnapshot.toCarQueue(): CarQueue {
            val parkedCars = ArrayList<ArrayList<ParkedCar>>()

            val queue: ArrayList<HashMap<String, Timestamp>> = get("carQueue") as ArrayList<HashMap<String, Timestamp>>
            queue.forEach { layer ->
                parkedCars.add(ArrayList<ParkedCar>())
                layer.forEach { (licensePlate, departureTime) ->
                    parkedCars.get(parkedCars.size - 1).add(ParkedCar(id, licensePlate, departureTime.toDate()))
                }
            }

            return CarQueue(parkedCars)
        }
    }
}