package com.ewdev.parkitnow.auth

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {

    private val authRepository: AuthRepository
    val userLiveData: MutableLiveData<FirebaseUser>
    val isLoggedInLiveData: MutableLiveData<Boolean>

    init {
        authRepository = AuthRepository()
        userLiveData = authRepository.userLiveData
        isLoggedInLiveData = authRepository.isLoggedInData
    }

//    fun requestIsLoggedInData() {
//
//        GlobalScope.launch {
//            val l
//        }
//
//        isLoggedInLiveData.postValue(this.userLiveData.value != null)
//    }
}