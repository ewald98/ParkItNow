package com.ewdev.parkitnow.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ewdev.parkitnow.data.ParkedCar
import com.ewdev.parkitnow.data.User
import com.ewdev.parkitnow.services.FirebaseService
import com.ewdev.parkitnow.utils.Helper
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class HomeUnparkedFragmentViewModel(application: Application): AndroidViewModel(application) {

    val firebaseUser = FirebaseAuth.getInstance().currentUser

    private lateinit var user: User
    private lateinit var userCar: ParkedCar

    private val _userCarLicensePlate: MutableLiveData<String> = MutableLiveData()

    val userCarLicensePlate: LiveData<String> get() = _userCarLicensePlate

    init {
        viewModelScope.launch {
            user = FirebaseService.getUser(firebaseUser!!.uid)!!

            userCar = FirebaseService.getCar(user.selectedCar!!)!!
            _userCarLicensePlate.value = userCar.licensePlate
        }
    }

}