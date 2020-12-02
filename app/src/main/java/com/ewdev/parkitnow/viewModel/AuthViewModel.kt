package com.ewdev.parkitnow.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ewdev.parkitnow.auth.AuthRepository
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

class AuthViewModel : ViewModel() {

    private val authRepository: AuthRepository
    val userLiveData: MutableLiveData<FirebaseUser>

    private val _isLoggedIn: MutableLiveData<Boolean> = MutableLiveData()
    val isLoggedIn : LiveData<Boolean> get() = _isLoggedIn

    init {
        authRepository = AuthRepository()
        userLiveData = authRepository.userLiveData
    }


    // manages the coroutines (if a viewModel is destroyed, we have to cancel started coroutines)
    private var viewModelJob = Job()

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }


    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)


    fun requestIsLoggedIn() {
        _isLoggedIn.value = this.userLiveData.value != null
//        isLoggedIn.postValue(this.userLiveData.value != null)
    }

}