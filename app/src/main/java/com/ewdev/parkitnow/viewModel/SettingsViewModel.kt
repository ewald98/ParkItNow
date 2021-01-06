package com.ewdev.parkitnow.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ewdev.parkitnow.services.FirebaseService
import com.ewdev.parkitnow.data.ParkedCar
import com.ewdev.parkitnow.data.User
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import java.util.*

class SettingsViewModel(application: Application): AndroidViewModel(application) {

    val changesCommited: MutableLiveData<Boolean> = MutableLiveData()

    fun setCarLicensePlate(licensePlate: String) {

        viewModelScope.launch {
            val user = FirebaseService.getUser(FirebaseAuth.getInstance().currentUser!!.uid)

            if (user!!.selectedCar!!.isEmpty()) {
                FirebaseService.run {
                    setCar(
                                ParkedCar(
                                    licensePlate,
                                    Calendar.getInstance(),
                                    listOf(),
                                    listOf()
                                )
                            )
                    updateUser(
                                User(
                                    user.uid,
                                    user.phoneNo,
                                    licensePlate,
                                    user.token,
                                    false
                                )
                            )
                }
                changesCommited.value = true
                return@launch
            }


            if (FirebaseService.exists(licensePlate)) {
                changesCommited.value = false
                return@launch
            }

            FirebaseService.deleteCar(user!!.selectedCar!!)

            FirebaseService.setCar(
                ParkedCar(
                    licensePlate,
                    Calendar.getInstance(),
                    listOf(),
                    listOf()
                )
            )
            FirebaseService.updateUser(
                User(
                    user.uid,
                    user.phoneNo,
                    licensePlate,
                    user.token,
                    false
                )
            )
            changesCommited.value = true
        }
    }

}