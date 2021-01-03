package com.ewdev.parkitnow.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ewdev.parkitnow.auth.FirebaseService
import com.ewdev.parkitnow.data.CarQueue
import com.ewdev.parkitnow.data.ParkedCar
import com.ewdev.parkitnow.data.RelativeParkedCar
import com.ewdev.parkitnow.data.User
import com.ewdev.parkitnow.utils.Helper
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList

class HomeFragmentViewModel(application: Application) : AndroidViewModel(application) {

    val firebaseUser = FirebaseAuth.getInstance().currentUser

//    private val dbRepository: DbRepository = DbRepository()

    private lateinit var user: User

    private val _isParked: MutableLiveData<Boolean> = MutableLiveData()
    private val _blockedCars: MutableLiveData<List<RelativeParkedCar>> = MutableLiveData()
    private val _blockerCars: MutableLiveData<List<RelativeParkedCar>> = MutableLiveData()
    private val _leaveTime: MutableLiveData<String> = MutableLiveData()

    val isParked: LiveData<Boolean> get() = _isParked
    val blockedCars: LiveData<List<RelativeParkedCar>> get() = _blockedCars
    val blockerCars: LiveData<List<RelativeParkedCar>> get() = _blockerCars
    val leaveTime: LiveData<String> get() = _leaveTime

    init {
        viewModelScope.launch {
            user = FirebaseService.getUser(firebaseUser!!.uid)!!

            _isParked.value = user.isParked

            val userCar = FirebaseService.getCar(user.selectedCar!!)
            _leaveTime.value = Helper.toStringDateFormat(userCar!!.departureTime)

            // get car queues.
            // for each car queue get blocker/blocked

            val cars = FirebaseService.getQueues(userCar!!.roots)

            val carQueue = CarQueue(CarQueue.carsToCarQueueFormat(cars), userCar.roots)

            Log.i("ce", "ce")

            val blockedCars = carQueue.getBlockedCars(user.selectedCar!!)
            _blockedCars.value = toViewFormat(blockedCars)
//
            val blockerCars = carQueue.getBlockerCars(user.selectedCar!!)
            _blockerCars.value = toViewFormat(blockerCars)

        }
    }

    fun publishCars() {
        _blockerCars.postValue(_blockerCars.value)
        _blockedCars.postValue(_blockedCars.value)
    }

    private fun toViewFormat(blockedCars: ArrayList<ParkedCar>): List<RelativeParkedCar> {
        val now = Date()

        blockedCars
                .sortWith(compareByDescending { car -> car.departureTime })

        val blockedCars2: ArrayList<RelativeParkedCar> = blockedCars
                .asReversed()
                .map { car ->
            RelativeParkedCar(
                    car.licensePlate,
                    Helper.toStringDateFormat(car.departureTime)
            )
        } as ArrayList<RelativeParkedCar>

        return blockedCars2
    }

}