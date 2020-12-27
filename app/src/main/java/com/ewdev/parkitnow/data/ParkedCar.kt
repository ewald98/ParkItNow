package com.ewdev.parkitnow.data

import android.os.Parcelable
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.android.parcel.Parcelize
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

@Parcelize
data class ParkedCar(
        val licensePlate: String,
        val departureTime: Date, // maybe import sth different?
        val roots: List<String>,
        val blocking: List<String>
) : Parcelable {

    companion object {

        fun DocumentSnapshot.toParkedCar(): ParkedCar {
            val blocking: List<String> = get("blocking") as List<String>
            val roots: List<String> = get("roots") as List<String>
            val departureTime = getDate("departureTime")!!
            // TODO: add phoneNO
            return ParkedCar(id, departureTime, roots, blocking)
        }

//        fun toFirebaseFormat():

    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ParkedCar

        if (licensePlate != other.licensePlate) return false
        if (departureTime != other.departureTime) return false
        if (roots != other.roots) return false
        if (blocking != other.blocking) return false

        return true
    }

    override fun hashCode(): Int {
        var result = licensePlate.hashCode()
        result = 31 * result + departureTime.hashCode()
        result = 31 * result + roots.hashCode()
        result = 31 * result + blocking.hashCode()
        return result
    }
}