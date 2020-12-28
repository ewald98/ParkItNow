package com.ewdev.parkitnow.data

import android.text.format.DateFormat
import java.util.*

data class DayTime(
        val hour: Int,
        val minute: Int
) {
    // TODO: should this and ParkedCar be data classes?

    // TODO: should be able to park for days?

    override fun toString(): String {
        val calendar: Calendar = Calendar.getInstance()
        calendar.set(0, 0, 0, hour, minute)
        return DateFormat.format("hh:mm aa", calendar).toString()
    }

    fun toDate(): Date {
        val date = Calendar.getInstance()
        date.set(Calendar.HOUR_OF_DAY, hour)
        date.set(Calendar.MINUTE, minute)

        val date2 = Date(date.timeInMillis)

        return date2
    }

}
