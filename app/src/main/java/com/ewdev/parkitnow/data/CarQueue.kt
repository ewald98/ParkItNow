package com.ewdev.parkitnow.data

import com.google.firebase.Timestamp
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class CarQueue(val carQueue: HashMap<String, ParkedCar>, val roots: List<String>) {

    // TODO("LATER: improve getBlockedCars & getBlockerCars to include more cases")

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

        return blockedCars.distinct() as ArrayList<ParkedCar>

    }

    fun getBlockerCars(carLicensePlate: String): ArrayList<ParkedCar> {

        var paths = ArrayList<ArrayList<ParkedCar>>()

        val blockedCars = carQueue
                .map { (_, car) -> car.blocking }
                .flatten()
                .distinct()

        val unobstructedCars = carQueue
                .map { (_, car) -> car.licensePlate }
                .filter { licensePlate -> !(licensePlate in blockedCars) }

        // init array with every unobstructedCar
        for (licensePlate in unobstructedCars) {
            paths.add(ArrayList())
            paths.last().add(carQueue[licensePlate]!!)
        }

        while (!foundAllPaths(paths, carLicensePlate, roots)) {
            val newPaths = ArrayList<ArrayList<ParkedCar>>()
            for (i in 0 until paths.size) {
                newPaths.addAll(growPath(paths[i], carLicensePlate))
            }
            paths = newPaths
        }

        paths = paths.filter { path ->
            (path.contains(carQueue[carLicensePlate]))
        } as ArrayList<ArrayList<ParkedCar>>

        val blockedCarsByUser = getBlockedCars(carLicensePlate)

        return paths
                .flatten()
                .distinct()
                .filter { car ->
                    !(car in blockedCarsByUser) && !(car.licensePlate == carLicensePlate)
                } as ArrayList<ParkedCar>

    }

    private fun growPath(path: ArrayList<ParkedCar>, carLicensePlate: String): ArrayList<ArrayList<ParkedCar>> {
        val newPaths = ArrayList<ArrayList<ParkedCar>>()

        if (lastElementIsRoot(path) || lastElementIsUserCar(path, carLicensePlate)) {
            newPaths.add(path)
            return newPaths
        }

        val blockedCars = path.last().blocking

        for (j in 0 until blockedCars.size) {
            val newPath = ArrayList<ParkedCar>()
            newPath.addAll(path)
            newPath.add(carQueue[blockedCars[j]]!!)
            newPaths.add(newPath)
        }

        return newPaths
    }

    private fun lastElementIsRoot(path: ArrayList<ParkedCar>) =
            path.last().blocking.isEmpty()

    private fun foundAllPaths(paths: ArrayList<ArrayList<ParkedCar>>, carLicensePlate: String, roots: List<String>): Boolean {

        for (path in paths) {
            // (not root) || (not my car)
            if (!lastElementIsUserCar(path, carLicensePlate) && !lastElementIsRoot(path))
                return false
        }

        return true
    }

    private fun lastElementIsUserCar(path: java.util.ArrayList<ParkedCar>, carLicensePlate: String) =
            path.last().licensePlate == carLicensePlate

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

        private fun moreLayersExist(currentLayer: ArrayList<ParkedCar>): Boolean {
            for (car in currentLayer) {
                if (car.blocking.isNotEmpty())
                    return true
            }
            return false
        }

    }

}