package com.ewdev.parkitnow.data

import android.os.Parcelable
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
    val uid: String,
    val phoneNo: String,
    val selectedCar: String?,
    val token: String,
    @field:JvmField
    val isParked: Boolean,
    @field:JvmField
    val leaveAnnouncer: Boolean,
    @field:JvmField
    val leaver: Boolean,
    val timesInQueue: Long
) : Parcelable {

    companion object {

        fun DocumentSnapshot.toUser(): User? {
            val phoneNo = getString("phoneNo")!!
            val selectedCar = getString("selectedCar")!!
            val token = getString("token")!!
            val isParked = getBoolean("isParked")!!
            val leaver = getBoolean("leaver")!!
            val leaveAnnouncer = getBoolean("leaveAnnouncer")!!
            val timesInQueue = getLong("timesInQueue")!!
            // document id is uid
            return User(id, phoneNo, selectedCar, token, isParked, leaveAnnouncer, leaver, timesInQueue)
        }

        fun User.toFirebaseFormat(): HashMap<String, Any> {
            val firebaseUser = HashMap<String, Any>()

            firebaseUser.put("phoneNo", phoneNo)
            firebaseUser.put("selectedCar", selectedCar ?: "")
            firebaseUser.put("token", token)
            firebaseUser.put("isParked", isParked)
            firebaseUser.put("leaver", leaver)
            firebaseUser.put("leaveAnnouncer", leaveAnnouncer)
            firebaseUser.put("timesInQueue", timesInQueue)

            return firebaseUser
        }
    }
}
