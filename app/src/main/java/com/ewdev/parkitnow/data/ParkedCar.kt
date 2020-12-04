package com.ewdev.parkitnow.data

import android.os.Parcelable
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.android.parcel.Parcelize
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

@Parcelize
data class ParkedCar(
    val queueId: String,
    val licensePlate: String,
    val departureTime: Date // maybe import sth different?
) : Parcelable {

    companion object {
        fun DocumentSnapshot.toParkedCar(): ParkedCar? {
            // TODO: doesn't match DB format
            val licensePlate = getString("licensePlate")!!
            // TODO: make departureTime of type Time or sth
            val departureTime = getDate("departureTime")!!
            // document id is uid
            return ParkedCar(id, licensePlate, departureTime)
        }

    }
}