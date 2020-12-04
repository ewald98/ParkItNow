package com.ewdev.parkitnow.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ewdev.parkitnow.auth.FirebaseService
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

    val _blockedCars: MutableLiveData<List<RelativeParkedCar>> = MutableLiveData()
    val _blockerCars: MutableLiveData<List<RelativeParkedCar>> = MutableLiveData()

    val blockedCars: LiveData<List<RelativeParkedCar>> get() = _blockedCars
    val blockerCars: LiveData<List<RelativeParkedCar>> get() = _blockerCars

    init {
        viewModelScope.launch {
            user = FirebaseService.getUser(firebaseUser!!.uid)!!
            val carQueue = FirebaseService.getCarQueue(user.queue!!)

            val blockedCars = carQueue.getBlockedCars(user.selectedCar!!)
            _blockedCars.value = toViewFormat(blockedCars)

            val blockerCars = carQueue.getBlockerCars(user.selectedCar!!)
            _blockerCars.value = toViewFormat(blockerCars)

        }
    }

    private fun toViewFormat(blockedCars: ArrayList<ParkedCar>): List<RelativeParkedCar> {
        val now = Date()
        val blockedCars2: ArrayList<RelativeParkedCar> = blockedCars.map { car ->
            RelativeParkedCar(
                    car.licensePlate,
                    Helper.toStringDateFormat(car.departureTime)
            )
        } as ArrayList<RelativeParkedCar>

        return blockedCars2
    }

}