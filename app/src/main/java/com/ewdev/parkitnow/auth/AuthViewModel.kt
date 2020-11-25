package com.ewdev.parkitnow.auth

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser

class AuthViewModel : ViewModel() {

    private val authRepository: AuthRepository
    val userLiveData: MutableLiveData<FirebaseUser>
    val isLoggedIn: MutableLiveData<Boolean> = MutableLiveData()

    init {
        authRepository = AuthRepository()
        userLiveData = authRepository.userLiveData
    }

    fun requestIsLoggedIn() {
        isLoggedIn.postValue(this.userLiveData.value != null)
    }
}