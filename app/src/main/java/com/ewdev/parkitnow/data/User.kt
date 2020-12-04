package com.ewdev.parkitnow.data

import android.os.Parcelable
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
    val uid: String,
    val phoneNo: String,
    val selectedCar: String?,
    @field:JvmField
    val isParked: Boolean,
    val queue: String?
) : Parcelable {

    companion object {
        fun DocumentSnapshot.toUser(): User? {
            val phoneNo = getString("phoneNo")!!
            val selectedCar = getString("selectedCar")!!
            val isParked = getBoolean("isParked")!!
            val queue = getString("queue")!!
            // document id is uid
            return User(id, phoneNo, selectedCar, isParked, queue)
        }
    }
}
