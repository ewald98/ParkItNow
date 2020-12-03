package com.ewdev.parkitnow.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*

class PhoneVerificationViewModel(application: Application, phoneNumber: String) : AndroidViewModel(application) {

    val smsCode: MutableLiveData<String> = MutableLiveData()
    val phoneVerified: MutableLiveData<Boolean> = MutableLiveData()

    val callbackLiveData: MutableLiveData<PhoneAuthProvider.OnVerificationStateChangedCallbacks> = MutableLiveData()


    lateinit var codeGenerated: String
    val context = getApplication<Application>().applicationContext

    fun requestAuthOptions() {
        callbackLiveData.value = callbacks
    }

    fun sendVerificationCode(phoneAuthOptions: PhoneAuthOptions) {
        PhoneAuthProvider.verifyPhoneNumber(phoneAuthOptions)
    }


    private val callbacks = object: PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

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

            // TODO: send toast
//            Toast.makeText(requireContext(), p0.message, Toast.LENGTH_SHORT).show()
        }
    }

    fun verifyCode(code: String) {

        val credential = PhoneAuthProvider.getCredential(codeGenerated, code)
        signInWithPhoneAuthCredential(credential)

    }
    init {
        requestAuthOptions()
    }


    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        val auth = FirebaseAuth.getInstance()

        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("signIn", "signInWithCredential:success")

                    phoneVerified.value = true

                    val user = task.result?.user
//                    Toast.makeText(context as Activity, "Verification successful", Toast.LENGTH_SHORT).show()

                } else {
                    // Sign in failed, display a message and update the UI
                    Log.w("signIn", "signInWithCredential:failure", task.exception)

                    phoneVerified.value = false

                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
//                        Toast.makeText(context as Activity, "Verification Invalid", Toast.LENGTH_SHORT).show()
                    }
                }
            }
    }

}