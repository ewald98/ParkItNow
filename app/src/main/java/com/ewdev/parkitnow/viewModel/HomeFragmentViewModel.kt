package com.ewdev.parkitnow.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ewdev.parkitnow.auth.FirebaseService
import com.ewdev.parkitnow.data.ParkedCar
import com.ewdev.parkitnow.data.User
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class HomeFragmentViewModel(application: Application) : AndroidViewModel(application) {

    val firebaseUser = FirebaseAuth.getInstance().currentUser

//    private val dbRepository: DbRepository = DbRepository()

    private lateinit var user: User

    val blockedCars: MutableLiveData<List<String>> = MutableLiveData()
    val blockedBy: MutableLiveData<List<ParkedCar>> = MutableLiveData()

    init {
        viewModelScope.launch {
            user = FirebaseService.getUser(firebaseUser!!.uid)!!
//            blockedCars.value = FirebaseService.getBlockedCars()
            blockedBy.value = FirebaseService.getBlockedBy(user.blockingQueue!!)
            // TODO: process incoming data and find where exactly the user's car is
            Log.i("ce", "ce")
        }
    }




}