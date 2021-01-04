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
) : Parcelable {

    companion object {

        fun DocumentSnapshot.toUser(): User? {
            val phoneNo = getString("phoneNo")!!
            val selectedCar = getString("selectedCar")!!
            val token = getString("token")!!
            val isParked = getBoolean("isParked")!!
            // document id is uid
            return User(id, phoneNo, selectedCar, token, isParked)
        }

        fun User.toFirebaseFormat(): HashMap<String, Any> {
            val firebaseUser = HashMap<String, Any>()

            firebaseUser.put("phoneNo", phoneNo)
            firebaseUser.put("isParked", isParked)
            firebaseUser.put("token", token)
            firebaseUser.put("selectedCar", selectedCar ?: "")

            return firebaseUser
        }
    }
}
