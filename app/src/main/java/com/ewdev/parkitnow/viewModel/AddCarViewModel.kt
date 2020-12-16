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

    fun requestCarValidity(car: ParkedCar) {
        viewModelScope.launch {
            validCar.value = FirebaseService.getCar(car.licensePlate) != null
        }
    }

}