package com.ewdev.parkitnow.auth

import android.os.Parcelable
import com.google.firebase.firestore.Exclude
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

@Parcelize
data class User(
    val id: String,
    val phoneNo: String
): Parcelable
