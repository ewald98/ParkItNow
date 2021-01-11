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

class ChangeLeaveTimeViewModel(application: Application) : AndroidViewModel(application) {

    private lateinit var userCar: ParkedCar
    private lateinit var user: User

    val leaveTime: MutableLiveData<Calendar> = MutableLiveData()

    val changesCommited: MutableLiveData<Unit> = MutableLiveData()



    init {
        val auth = FirebaseAuth.getInstance()

        viewModelScope.launch {
            user = FirebaseService.getUser(auth.currentUser!!.uid)!!
            userCar = FirebaseService.getCar(user.selectedCar!!)!!
        }

    }

    fun onTimeSet(calendar: Calendar) {
        leaveTime.value = calendar
    }

    fun changeLeaveTime() {

        GlobalScope.launch {
            // TODO: make sure date is set and that it's not 10 mins before leaving (or if it is,
            // go to the leaveNow useCase

            // get roots from all blocked cars
            FirebaseService.updateCar(
                ParkedCar(
                    user.selectedCar!!,
                    leaveTime.value!!,
                    userCar.roots,
                    userCar.blocking,
                    true
                )
            )

            FirebaseService.updateUser(
                User(
                    user.uid,
                    user.phoneNo,
                    user.selectedCar!!,
                    user.token,
                    true,
                    false,
                    false
                )
            )
            changesCommited.postValue(Unit)

        }
    }

}