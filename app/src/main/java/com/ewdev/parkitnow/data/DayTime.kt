package com.ewdev.parkitnow.data

import android.text.format.DateFormat
import java.util.*

data class DayTime(
        val hour: Int,
        val minute: Int
) {
    override fun toString(): String {
        val calendar: Calendar = Calendar.getInstance()
        calendar.set(0, 0, 0, hour, minute)
        return DateFormat.format("hh:mm aa", calendar).toString()
    }
}
