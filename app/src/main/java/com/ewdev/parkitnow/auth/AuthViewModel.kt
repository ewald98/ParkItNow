package com.ewdev.parkitnow.auth

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser

class AuthViewModel(application: Application) : AndroidViewModel(application) {

    lateinit var authRepository: AuthRepository
    lateinit var userLiveData: MutableLiveData<FirebaseUser>

    init {
        authRepository = AuthRepository(application)
        userLiveData = authRepository.userLiveData
    }



}