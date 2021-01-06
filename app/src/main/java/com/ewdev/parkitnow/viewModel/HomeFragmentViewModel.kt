package com.ewdev.parkitnow.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ewdev.parkitnow.data.*
import com.ewdev.parkitnow.services.FirebaseService
import com.ewdev.parkitnow.utils.Helper
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import java.lang.Math.abs
import java.util.*
import kotlin.collections.ArrayList

class HomeFragmentViewModel(application: Application) : AndroidViewModel(application) {

    private lateinit var rawBlockedCars: ArrayList<ParkedCar>
    private lateinit var rawBlockerCars: ArrayList<ParkedCar>
    val firebaseUser = FirebaseAuth.getInstance().currentUser

//    private val dbRepository: DbRepository = DbRepository()

    private lateinit var user: User
    private lateinit var userCar: ParkedCar

    private val _blockedCars: MutableLiveData<List<RelativeParkedCar>> = MutableLiveData()
    private val _blockerCars: MutableLiveData<List<RelativeParkedCar>> = MutableLiveData()
    private val _leaveTime: MutableLiveData<String> = MutableLiveData()
    private val _phoneNo: MutableLiveData<String> = MutableLiveData()

    private val _carState: MutableLiveData<CarState> = MutableLiveData()
    private val _carLeavingLicensePlate: MutableLiveData<String> = MutableLiveData()
    private val _refreshFragment: MutableLiveData<Unit> = MutableLiveData()

    val blockedCars: LiveData<List<RelativeParkedCar>> get() = _blockedCars
    val blockerCars: LiveData<List<RelativeParkedCar>> get() = _blockerCars
    val leaveTime: LiveData<String> get() = _leaveTime
    val phoneNo: LiveData<String> get() = _phoneNo

    val carState: LiveData<CarState> get() = _carState
    val carLeavingLicensePlate: LiveData<String> get() = _carLeavingLicensePlate
    val refreshFragment: MutableLiveData<Unit> = MutableLiveData()

    init {
        viewModelScope.launch {
            user = FirebaseService.getUser(firebaseUser!!.uid)!!

            userCar = FirebaseService.getCar(user.selectedCar!!)!!
            _leaveTime.value = Helper.toStringDateFormat(userCar.departureTime)

            val cars = FirebaseService.getCars(userCar.roots)
            val carQueue = CarQueue(CarQueue.carsToCarQueueFormat(cars), userCar.roots)

            rawBlockedCars = carQueue.getBlockedCars(user.selectedCar!!)
            _blockedCars.value = toViewFormat(rawBlockedCars)
            rawBlockerCars = carQueue.getBlockerCars(user.selectedCar!!)
            _blockerCars.value = toViewFormat(rawBlockerCars)


            // TODO : DON'T FORGET ABOUT DISCARDING LEAVER LEAVEANOUUNCER AFTER "I LEFT"
            //  _isParked.value = user.isParked
            if (user.leaver) {
                // TODO: LATER allow leaver to change his mind (add another button for "stay longer")
                // inflate leaver buttons
                _carState.value = CarState.LEAVER
            } else if (user.leaveAnnouncer) {
                // TODO: LATER: allow viewModel to access UI
                // inflate car leaving license plate
                // inflate leaveAnnouncer buttons
                val leaverCars = rawBlockedCars.filter { car ->
                    abs(car.departureTime.time.time - Date().time) / 1000 <= 300
                }
                // TODO: LATER, for the moment there can only be one leaverCar
                _carLeavingLicensePlate.value = leaverCars[0].licensePlate
                _carState.value = CarState.LEAVE_ANNOUNCER
            } else {
                // nothing
            }


        }
    }

    fun publishCars() {
        _blockerCars.postValue(_blockerCars.value)
        _blockedCars.postValue(_blockedCars.value)
    }

    private fun toViewFormat(blockedCars: ArrayList<ParkedCar>): List<RelativeParkedCar> {
        blockedCars
                .sortWith(compareByDescending { car -> car.departureTime })

        val blockedCars2: ArrayList<RelativeParkedCar> = blockedCars
                .asReversed()
                .map { car ->
            RelativeParkedCar(
                    car.licensePlate,
                    Helper.toStringDateFormat(car.departureTime)
            )
        } as ArrayList<RelativeParkedCar>

        return blockedCars2
    }

    fun leaveNow() {

        viewModelScope.launch {
            FirebaseService.updateCar(
                ParkedCar(
                    userCar.licensePlate,
                    Calendar.getInstance(),
                    userCar.roots,
                    userCar.blocking
                )
            )
        }
        refreshFragment()
    }

    fun refreshFragment() {
        _refreshFragment.value = Unit
    }

    fun getUserPhoneNumber(licensePlate: String) {
        viewModelScope.launch {
            val user = FirebaseService.getUserBySelectedCar(licensePlate)
            if (user != null) {
                _phoneNo.value = user.phoneNo
            }
        }
    }

    fun cleanQueue() {
         TODO("URGENT")
        // sets leaver and leaveAnnouncers back to false
//        val allCars = arrayListOf<ParkedCar>()
//        allCars.addAll(rawBlockedCars)
//        allCars.addAll(rawBlockerCars)
//
//        getUsers(allCars)
    }

}