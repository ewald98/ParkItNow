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
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.lang.Math.abs
import java.util.*
import kotlin.collections.ArrayList

class HomeParkedFragmentViewModel(application: Application) : AndroidViewModel(application) {

    private lateinit var carQueue: CarQueue
    private lateinit var rawBlockedCars: ArrayList<ParkedCar>
    private lateinit var rawBlockerCars: ArrayList<ParkedCar>
    val firebaseUser = FirebaseAuth.getInstance().currentUser

//    private val dbRepository: DbRepository = DbRepository()

    private lateinit var user: User
    private lateinit var userCar: ParkedCar

    private val _blockedCars: MutableLiveData<List<RelativeParkedCar>> = MutableLiveData()
    private val _blockerCars: MutableLiveData<List<RelativeParkedCar>> = MutableLiveData()
    private val _leaveTime: MutableLiveData<String> = MutableLiveData()
    private val _userCarLicensePlate: MutableLiveData<String> = MutableLiveData()
    private val _phoneNo: MutableLiveData<String> = MutableLiveData()
    private val _isParked: MutableLiveData<Boolean> = MutableLiveData()

    private val _carState: MutableLiveData<CarState> = MutableLiveData()
    private val _carLeavingLicensePlate: MutableLiveData<String> = MutableLiveData()
    private val _refreshFragment: MutableLiveData<Unit> = MutableLiveData()

    val blockedCars: LiveData<List<RelativeParkedCar>> get() = _blockedCars
    val blockerCars: LiveData<List<RelativeParkedCar>> get() = _blockerCars
    val leaveTime: LiveData<String> get() = _leaveTime
    val userCarLicensePlate: LiveData<String> get() = _userCarLicensePlate
    val phoneNo: LiveData<String> get() = _phoneNo
    val isParked: LiveData<Boolean> get() = _isParked

    val carState: LiveData<CarState> get() = _carState
    val carLeavingLicensePlate: LiveData<String> get() = _carLeavingLicensePlate
    val refreshFragment: MutableLiveData<Unit> get() = _refreshFragment

    init {

        viewModelScope.launch {
            user = FirebaseService.getUser(firebaseUser!!.uid)!!
            _isParked.value = user.isParked

            userCar = FirebaseService.getCar(user.selectedCar!!)!!
            _userCarLicensePlate.value = userCar.licensePlate
            _leaveTime.value = Helper.toStringDateFormat(userCar.departureTime, true)

            val cars = FirebaseService.getCars(userCar.roots)
            carQueue = CarQueue(CarQueue.carsToCarQueueFormat(cars), userCar.roots)

            rawBlockedCars = carQueue.getBlockedCars(user.selectedCar!!)
            _blockedCars.value = toViewFormat(rawBlockedCars)
            rawBlockerCars = carQueue.getBlockerCars(user.selectedCar!!)
            _blockerCars.value = toViewFormat(rawBlockerCars)


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
                // TODO: LATER, for the moment there can only be one leaverCar at an instance
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
                    userCar.blocking,
                    true,    // still true
                )
            )
            FirebaseService.updateUser(
                User(
                    user.uid,
                    user.phoneNo,
                    user.selectedCar,
                    user.token,
                    true,
                    false,
                    true
                )
            )
            refreshFragment()
        }
    }

    fun refreshFragment() {
        _refreshFragment.postValue(Unit)
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
        GlobalScope.launch {
            var leaverUser: User
            var leaverCar: ParkedCar
            if (user.leaver) {
                leaverUser = user
                leaverCar = userCar
            } else {
                val leaver = _carLeavingLicensePlate.value
                leaverUser = FirebaseService.getUserBySelectedCar(leaver!!)!!
                leaverCar = FirebaseService.getCar(leaver)!!
            }
            val blockerCars = carQueue.getBlockerCars(leaverUser.selectedCar!!)
            FirebaseService.updateUser(
                User(
                    leaverUser.uid,
                    leaverUser.phoneNo,
                    leaverUser.selectedCar,
                    leaverUser.token,
                    false,  // no longer parked
                    false,
                    false
                )
            )
            FirebaseService.updateCar(
                ParkedCar(
                    leaverCar.licensePlate,
                    leaverCar.departureTime,
                    leaverCar.roots,
                    leaverCar.blocking,
                    false
                )
            )

            for (blockerCar in blockerCars) {
                val blockerUser = FirebaseService.getUserBySelectedCar(blockerCar.licensePlate)!!
                FirebaseService.updateUser(
                    User(
                        blockerUser.uid,
                        blockerUser.phoneNo,
                        blockerUser.selectedCar,
                        blockerUser.token,
                        false,  // no longer parked
                        false,
                        false
                    )
                )
                FirebaseService.updateCar(
                    ParkedCar(
                        blockerCar.licensePlate,
                        blockerCar.departureTime,
                        blockerCar.roots,
                        blockerCar.blocking,
                        false
                    )
                )
            }

            refreshFragment()
        }
    }

}