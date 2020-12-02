package com.ewdev.parkitnow.viewModel

import android.app.Application
import android.os.Parcel
import android.os.Parcelable
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class PhoneVerificationViewModelFactory(val application: Application, val phoneNumber: String)
    : ViewModelProvider.AndroidViewModelFactory(application) {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return PhoneVerificationViewModel(application, phoneNumber) as T
    }

}