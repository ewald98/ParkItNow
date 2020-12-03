package com.ewdev.parkitnow.data

data class User(
    val uid: String,
    val phoneNo: String,
    val selectedCar: String?,
    @field:JvmField
    val isParked: Boolean
)
