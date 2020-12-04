package com.ewdev.parkitnow.data

import com.ewdev.parkitnow.utils.Helper
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class CarQueue(val carQueue: ArrayList<ArrayList<ParkedCar>>) {

    fun getBlockedCars(carLicensePlate: String): ArrayList<ParkedCar> {
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

        return blockedCars
    }

    fun getBlockerCars(carLicensePlate: String): ArrayList<ParkedCar> {
        val blockerCars = ArrayList<ParkedCar>()

        val carLayer = getCarLayer(carLicensePlate)
        for (i in carQueue.size - 1 downTo carLayer + 1)  {
            carQueue[i].forEach { car -> blockerCars.add(car) }
        }

        return blockerCars
    }

    fun isInLayer(carLicensePlate: String, layer: ArrayList<ParkedCar>): Boolean {
        return layer.any { parkedCar -> carLicensePlate.equals(parkedCar.licensePlate) }
    }

    fun getCarLayer(carLicensePlate: String): Int {
        return carQueue.indexOfFirst{ layer: ArrayList<ParkedCar> -> isInLayer(carLicensePlate, layer)}
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