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

class SettingsViewModel(application: Application) : AndroidViewModel(application) {

    private lateinit var user: User

    val changesCommited: MutableLiveData<Boolean> = MutableLiveData()
    val selectedCar: MutableLiveData<String> = MutableLiveData()

    init {
        viewModelScope.launch {
            user = FirebaseService.getUser(FirebaseAuth.getInstance().currentUser!!.uid)!!
            selectedCar.value = user!!.selectedCar!!
        }
    }

    fun setCarLicensePlate(licensePlate: String) {

        viewModelScope.launch {

            if (user!!.selectedCar!!.isEmpty()) {
                FirebaseService.run {
                    setCar(
                        ParkedCar(
                            licensePlate,
                            Calendar.getInstance(),
                            listOf(),
                            listOf(),
                            false   // must be false
                        )
                    )
                    updateUser(
                        User(
                            user.uid,
                            user.phoneNo,
                            licensePlate,
                            user.token,
                            false,
                            user.leaveAnnouncer,
                            user.leaver,
                            user.timesInQueue
                        )
                    )
                }
                selectedCar.value = licensePlate
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
                    listOf(),
                    false   // must be false
                )
            )
            FirebaseService.updateUser(
                User(
                    user.uid,
                    user.phoneNo,
                    licensePlate,
                    user.token,
                    false,
                    user.leaveAnnouncer,
                    user.leaver,
                    user.timesInQueue
                )
            )
            selectedCar.value = licensePlate
            changesCommited.value = true
        }
    }

}