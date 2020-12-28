package com.ewdev.parkitnow.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ewdev.parkitnow.auth.FirebaseService
import com.ewdev.parkitnow.data.ParkedCar
import kotlinx.coroutines.launch

class AddCarViewModel(application: Application): AndroidViewModel(application) {

    val validCar: MutableLiveData<Boolean> = MutableLiveData()
    val car: MutableLiveData<ParkedCar> = MutableLiveData()

    fun requestCarValidity(carId: String) {
        viewModelScope.launch {
            val verifiedCar = FirebaseService.getCar(carId)
            validCar.value = verifiedCar != null
            if (verifiedCar != null) {
                car.value = verifiedCar
            }
        }
    }

}