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
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class HomeFragmentViewModel(application: Application) : AndroidViewModel(application) {

    val firebaseUser = FirebaseAuth.getInstance().currentUser

//    private val dbRepository: DbRepository = DbRepository()

    private lateinit var user: User
    private lateinit var carQueue: List<ParkedCar>


    val _blockedCars: MutableLiveData<List<RelativeParkedCar>> = MutableLiveData()
    val _blockerCars: MutableLiveData<List<RelativeParkedCar>> = MutableLiveData()

    val blockedCars: LiveData<List<RelativeParkedCar>> get() = _blockedCars
    val blockerCars: LiveData<List<RelativeParkedCar>> get() = _blockerCars

    init {
        viewModelScope.launch {
            user = FirebaseService.getUser(firebaseUser!!.uid)!!
//            blockedCars.value = FirebaseService.getBlockedCars()
            val carQueue = FirebaseService.getCarQueue(user.queue!!)
            // TODO: process incoming data and find where exactly the user's car is
            _blockedCars.value = carQueue.getBlockedCars(user.selectedCar!!)

        }
    }

    private fun getBlockedCars(carQueue: List<ParkedCar>): List<RelativeParkedCar> {
        val relativeParkedCars = ArrayList<RelativeParkedCar>()




        return relativeParkedCars
    }


}