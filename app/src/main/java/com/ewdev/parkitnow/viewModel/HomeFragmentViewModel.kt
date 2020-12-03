package com.ewdev.parkitnow.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.ewdev.parkitnow.auth.AuthRepository
import com.ewdev.parkitnow.auth.DbRepository
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import java.lang.Appendable

class HomeFragmentViewModel(application: Application) : AndroidViewModel(application) {

    private val dbRepository: DbRepository = DbRepository()

    private var viewModelJob = Job()

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)




}