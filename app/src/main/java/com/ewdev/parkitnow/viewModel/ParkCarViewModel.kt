package com.ewdev.parkitnow.viewModel

import android.app.Application
import android.app.TimePickerDialog
import android.text.format.DateFormat
import android.widget.TimePicker
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.ewdev.parkitnow.data.DayTime
import com.ewdev.parkitnow.utils.Helper
import kotlinx.android.synthetic.main.fragment_park_car.*
import java.util.*

class ParkCarViewModel(application: Application) : AndroidViewModel(application) {

    val leaveTime: MutableLiveData<DayTime> = MutableLiveData()

    fun onTimeSet(hourOfDay: Int, minute: Int) {
        leaveTime.value = DayTime(hourOfDay, minute)

        // TODO: make sure (now > date)
    }

}