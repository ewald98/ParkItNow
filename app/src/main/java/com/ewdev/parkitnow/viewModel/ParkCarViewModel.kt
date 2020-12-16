package com.ewdev.parkitnow.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ewdev.parkitnow.auth.FirebaseService
import com.ewdev.parkitnow.data.DayTime
import com.ewdev.parkitnow.data.ParkedCar
import com.ewdev.parkitnow.data.RelativeParkedCar
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.collections.ArrayList

class ParkCarViewModel(application: Application) : AndroidViewModel(application) {

    val leaveTime: MutableLiveData<DayTime> = MutableLiveData()
    val blockedCars: MutableLiveData<List<ParkedCar>> = MutableLiveData()

    val _blockedCars: ArrayList<ParkedCar> = ArrayList()

    fun onTimeSet(hourOfDay: Int, minute: Int) {
        leaveTime.value = DayTime(hourOfDay, minute)

        // TODO: make sure (now > date)
    }

    fun removeFromList(car: ParkedCar) {
        // TODO : remove from list
    }

    fun addCar(car: ParkedCar) {
        _blockedCars.add(car)
        retrieveCars()
    }

    fun retrieveCars() {
        blockedCars.postValue(_blockedCars)
    }

    fun parkCar() {
//        _blockedCars
//        leaveTime
        GlobalScope.launch {
            // TODO: commit changes
        }
    }

}