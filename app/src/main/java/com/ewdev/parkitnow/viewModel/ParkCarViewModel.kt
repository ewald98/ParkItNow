package com.ewdev.parkitnow.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ewdev.parkitnow.services.FirebaseService
import com.ewdev.parkitnow.data.ParkedCar
import com.ewdev.parkitnow.data.User
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList

class ParkCarViewModel(application: Application) : AndroidViewModel(application) {

    private lateinit var user: User
    private var timeSet: Boolean = false

    val leaveTime: MutableLiveData<Calendar> = MutableLiveData()
    val blockedCars: MutableLiveData<List<ParkedCar>> = MutableLiveData()

    val changesCommited: MutableLiveData<Unit> = MutableLiveData()
    val timeNotSet: MutableLiveData<Unit> = MutableLiveData()

    val _blockedCars: ArrayList<ParkedCar> = ArrayList()


    init {
        val auth = FirebaseAuth.getInstance()

        viewModelScope.launch {
            user = FirebaseService.getUser(auth.currentUser!!.uid)!!
        }
    }

    fun onTimeSet(calendar: Calendar) {
        leaveTime.value = calendar
        timeSet = true

        // TODO: make sure (now < date)

    }

    fun removeFromList(car: ParkedCar) {
        _blockedCars.remove(car)
        blockedCars.postValue(_blockedCars)
    }

    fun addCar(car: ParkedCar) {
        _blockedCars.add(car)
        retrieveCars()
    }

    fun retrieveCars() {
        blockedCars.postValue(_blockedCars)
    }

    fun parkCar() {
        if (!timeSet) {
            timeNotSet.postValue(Unit)
        } else {
            GlobalScope.launch {
                // TODO: update parked in User (FOR DEBUGGING, DO NOT YET)

                // get roots from all blocked cars
                val roots = _blockedCars
                    .map { car -> car.roots }
                    .flatten()
                    .distinct()
                    .ifEmpty {
                        listOf(user.selectedCar)
                    }

                val blocking = _blockedCars
                    .map { car -> car.licensePlate }

                FirebaseService.updateCar(
                    ParkedCar(
                        user.selectedCar!!,
                        leaveTime.value!!,
                        roots as List<String>,
                        blocking,
                        true
                    )
                )

                FirebaseService.updateUser(
                    User(
                        user.uid!!,
                        user.phoneNo!!,
                        user.selectedCar!!,
                        user.token!!,
                        true,
                        false,
                        false,
                        user.timesInQueue + 1
                    )
                )
                changesCommited.postValue(Unit)

            }
        }
    }

}