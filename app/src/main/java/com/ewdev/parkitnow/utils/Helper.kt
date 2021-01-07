package com.ewdev.parkitnow.utils

import android.text.format.DateFormat
import kotlinx.android.synthetic.main.fragment_park_car.*
import java.text.SimpleDateFormat
import java.util.*

object Helper {

    fun toStringDateFormat(departureDate: Calendar): String {
        return toStringDateFormat(departureDate, false)
    }

    fun toStringDateFormat(departureDate: Calendar, absoluteTime: Boolean): String {

        val now = Date()
        // TODO: what if departure time < now? treat it in viewmodel maybe...
        val milliseconds = departureDate.time.time - now.time

        val seconds = milliseconds / 1000
        val minutes = seconds / 60
        val hours = minutes / 60
        val days = hours / 24

        if (days > 1) {
            // TODO: make this more beautiful with DateFormat
            return "" + DateFormat.format("HH:mm, MM-dd", departureDate)
        } else {
            if (absoluteTime)
                return "" + DateFormat.format("HH:mm", departureDate)
            if (hours.toInt() == 0)
                return "" + minutes + "m"
            else
                return "" + hours + "h " + (minutes - hours * 60) + "m"
        }
    }

}