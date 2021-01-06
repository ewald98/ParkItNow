package com.ewdev.parkitnow.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ewdev.parkitnow.auth.AuthRepository
import com.ewdev.parkitnow.services.FirebaseService
import com.ewdev.parkitnow.data.User
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {

    private val authRepository: AuthRepository
    val userLiveData: MutableLiveData<FirebaseUser>

    private val _isParked: MutableLiveData<Boolean> = MutableLiveData()
    private val _isLoggedIn: MutableLiveData<Boolean> = MutableLiveData()

    val isLoggedIn : LiveData<Boolean> get() = _isLoggedIn
    val isParked: LiveData<Boolean> get() = _isParked

    private lateinit var user: User

    init {
        authRepository = AuthRepository()
        userLiveData = authRepository.userLiveData
    }

    // manages the coroutines (if a viewModel is destroyed, we have to cancel started coroutines)
    fun requestIsLoggedIn() {
        _isLoggedIn.value = this.userLiveData.value != null
//        isLoggedIn.postValue(this.userLiveData.value != null)
    }

    fun requestIsParked() {
        viewModelScope.launch {
            user = FirebaseService.getUser(userLiveData.value!!.uid)!!

            _isParked.value = user.isParked
        }
    }
}