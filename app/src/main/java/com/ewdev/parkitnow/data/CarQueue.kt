package com.ewdev.parkitnow.data

import com.google.firebase.Timestamp
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class CarQueue(val carQueue: HashMap<String, ParkedCar>, val roots: List<String>) {

    fun getBlockedCars(carLicensePlate: String): ArrayList<ParkedCar> {

        val blockedCars = ArrayList<ParkedCar>()

        var currentCarLicensePlates = arrayListOf<String>(carLicensePlate)
        var currentBlocking: ArrayList<ParkedCar>
        while (currentCarLicensePlates.isNotEmpty()) {

            currentBlocking = ArrayList()
            for (currentCarLicensePlate in currentCarLicensePlates) {

                val blocking = carQueue[currentCarLicensePlate]!!.blocking

                for (blockerCarLicensePlate in blocking) {
                    val car = carQueue[blockerCarLicensePlate]
                    if (!currentBlocking.contains(car))
                        currentBlocking.add(car!!)
                }
            }

            currentCarLicensePlates = currentBlocking.map { car -> car.licensePlate } as ArrayList<String>

            blockedCars.addAll(currentBlocking)
        }

        return blockedCars

//
//        for ((i, layer) in carQueue.withIndex()) {
//            if (isInLayer(carLicensePlate, layer)) {
//                break;
//            } else {
//                layer.forEach { car ->
//                    blockedCars.add(car)
//                }
//            }
//        }
//
//        return blockedCars
    }

    fun getBlockerCars(carLicensePlate: String): ArrayList<ParkedCar> {
        TODO("REDO")
//        val blockerCars = ArrayList<ParkedCar>()
//
//        val carLayer = getCarLayer(carLicensePlate)
//        for (i in carQueue.size - 1 downTo carLayer + 1)  {
//            carQueue[i].forEach { car -> blockerCars.add(car) }
//        }
//
//        return blockerCars
    }

    fun isInLayer(carLicensePlate: String, layer: ArrayList<ParkedCar>): Boolean {
        TODO("REDO")
//        return layer.any { parkedCar -> carLicensePlate.equals(parkedCar.licensePlate) }
    }

    fun getCarLayer(carLicensePlate: String): Int {
        TODO("REDO")
//        return carQueue.indexOfFirst{ layer: ArrayList<ParkedCar> -> isInLayer(carLicensePlate, layer)}
    }

    companion object {

        fun mergeQueues() {

        }

        fun carsToCarQueueFormat(cars: List<ParkedCar>): HashMap<String, ParkedCar> {

            val carMap = HashMap<String, ParkedCar>()

            for (car in cars) {
                carMap.put(car.licensePlate, car)
            }

            return carMap

        }

//        fun carsToCarQueue(cars: List<ParkedCar>, roots: List<String>, userCar: ParkedCar): ArrayList<ArrayList<ParkedCar>> {
//
//            val queue = ArrayList<ArrayList<ParkedCar>>()
//            val _cars: ArrayList<ParkedCar> = cars.toMutableList() as ArrayList<ParkedCar>
//
//            var currentLayer: ArrayList<ParkedCar>
//            var currentCarLicensePlates: List<String> = emptyList()
//
//            var toBeRemoved = ArrayList<ParkedCar>()
//
////            while (moreLayersExist(currentLayer)) {
////                for (car in currentLayer)
////            }
////
////            queue.reverse()
//
//            do {
//                currentLayer = ArrayList()
//
//                for (car in _cars) {
//                    if (car.licensePlate in roots || car.blocking.any{ blockedCar -> blockedCar in currentCarLicensePlates}) {
//                        currentLayer.add(car)
//                        toBeRemoved.add(car)
//                    }
//                }
//                _cars.removeAll(toBeRemoved)
//                queue.add(currentLayer)
//                currentCarLicensePlates = currentLayer.map { car -> car.licensePlate }
//
//                toBeRemoved = ArrayList()
//            } while (_cars.isNotEmpty())
//
//            return queue
//        }

        private fun moreLayersExist(currentLayer: ArrayList<ParkedCar>): Boolean {
            for (car in currentLayer) {
                if (car.blocking.isNotEmpty())
                    return true
            }
            return false
        }

    }

    fun toFirebaseFormat(): ArrayList<HashMap<String, Timestamp>> {
        TODO("UPDATE")
//        val firebaseCars: ArrayList<HashMap<String, Timestamp>> = ArrayList()
//
//        for (layer in carQueue) {
//            val firebaseMap: HashMap<String, Timestamp> = HashMap()
//            for (car in layer) {
//                firebaseMap.put(car.licensePlate, Timestamp(car.departureTime))
//            }
//            firebaseCars.add(firebaseMap)
//        }
//
//        return firebaseCars
    }

}