package com.ewdev.parkitnow.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData

class ParkCarViewModel(application: Application) : AndroidViewModel(application) {

//    val _hour: MutableLiveData<Int> = MutableLiveData()
//    val _minute: MutableLiveData<Int> = MutableLiveData()
    var _hour: Int = 12
    var _minute: Int = 0

    fun setSelectedTime(hour: Int, minute: Int) {
        _hour = hour
        _minute = minute
    }

}