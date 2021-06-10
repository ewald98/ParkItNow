package com.ewdev.parkitnow.viewModel

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ewdev.parkitnow.services.FirebaseService
import com.ewdev.parkitnow.data.User
import com.ewdev.parkitnow.utils.Constants
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class PhoneVerificationViewModel(application: Application, val phoneNumber: String) :
    AndroidViewModel(application) {

    val smsCode: MutableLiveData<String> = MutableLiveData()
    val phoneVerified: MutableLiveData<Boolean> = MutableLiveData()
    val verificationFailed: MutableLiveData<String> = MutableLiveData()

    val callbackLiveData: MutableLiveData<PhoneAuthProvider.OnVerificationStateChangedCallbacks> =
        MutableLiveData()

    private val _isParked: MutableLiveData<Boolean> = MutableLiveData()
    val isParked: LiveData<Boolean> get() = _isParked

    private var user: User? = null

    var codeGenerated: String = Constants.DEFAULT_SMS_GENERATED_CODE
    @SuppressLint("StaticFieldLeak")
    val context = getApplication<Application>().applicationContext

    init {
//        requestAuthOptions()
    }

    fun requestAuthOptions() {
        callbackLiveData.value = callbacks
    }

    fun sendVerificationCode(phoneAuthOptions: PhoneAuthOptions) {
        PhoneAuthProvider.verifyPhoneNumber(phoneAuthOptions)
    }


    private val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
            super.onCodeSent(p0, p1)
            codeGenerated = p0
        }

        override fun onVerificationCompleted(p0: PhoneAuthCredential) {
            if (p0.smsCode != null) {
                Log.d("PhoneVerification", "received smsCode: " + p0.smsCode)
                smsCode.value = p0.smsCode.toString()
                verifyCode(p0.smsCode!!)
            }
        }

        override fun onVerificationFailed(p0: FirebaseException) {
            Log.d("PhoneVerification", p0.toString())

            verificationFailed.value = p0.message
        }
    }

    fun verifyCode(code: String) {

        val credential = PhoneAuthProvider.getCredential(codeGenerated, code)
        signInWithPhoneAuthCredential(credential)

    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        val auth = FirebaseAuth.getInstance()

        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("signIn", "signInWithCredential:success")

                    phoneVerified.value = true

                    viewModelScope.launch {
                        user = FirebaseService.getUser(auth.currentUser!!.uid)

                        if (user == null) {

                            val token = FirebaseMessaging.getInstance().token.await()
                            val addedUser = FirebaseService.addNewUser(
                                auth.currentUser!!.uid,
                                phoneNumber,
                                token!!
                            )
                            if (addedUser)
                                _isParked.value = false

                            // Log and toast
//                                    val msg = getString(R.string.msg_token_fmt, token)
//                                    Log.d(TAG, msg)

                        } else {
                            val newToken = FirebaseMessaging.getInstance().token.await()
                            FirebaseService.updateUser(
                                User(
                                    user!!.uid,
                                    phoneNumber,
                                    user!!.selectedCar,
                                    newToken,
                                    user!!.isParked,
                                    user!!.leaveAnnouncer,
                                    user!!.leaver,
                                    user!!.timesInQueue
                                )
                            )

                            _isParked.value = user!!.isParked
                        }
                    }

                    val user = task.result?.user
//                    Toast.makeText(context as Activity, "Verification successful", Toast.LENGTH_SHORT).show()

                } else {
                    // Sign in failed, display a message and update the UI
                    Log.w("signIn", "signInWithCredential:failure", task.exception)


                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                        verificationFailed.value = task.toString()
                    } else {
                        phoneVerified.value = false
                    }
                }
            }
    }

}