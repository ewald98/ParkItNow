package com.ewdev.parkitnow.view.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.ewdev.parkitnow.R
import com.ewdev.parkitnow.view.fragments.PhoneVerificationFragmentArgs
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import kotlinx.android.synthetic.main.fragment_phone_verification.*
import java.util.concurrent.TimeUnit

class PhoneVerificationFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_phone_verification, container, false)
    }

    val args: PhoneVerificationFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        next_button.setOnClickListener {

            val code = pin_view.text.toString()
            if (!code.isEmpty()) {
                verifyCode(code)
            }
        }

        val phoneNumber = args.phoneNumber
        Log.i("phone_number_received", phoneNumber)

        sendVerificationCode(phoneNumber)

    }

    private fun sendVerificationCode(phoneNumber: String) {

        PhoneAuthProvider.verifyPhoneNumber(PhoneAuthOptions.newBuilder()
                .setPhoneNumber(phoneNumber)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(requireActivity())
                .setCallbacks(callbacks)
                .build())

    }

    lateinit var codeGenerated: String

    private val callbacks = object: PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
            super.onCodeSent(p0, p1)
            codeGenerated = p0

        }

        override fun onVerificationCompleted(p0: PhoneAuthCredential) {
            if (p0.smsCode != null) {
                pin_view.setText(p0.smsCode)
                verifyCode(p0.smsCode!!)
            }
        }

        override fun onVerificationFailed(p0: FirebaseException) {
            Toast.makeText(requireContext(), p0.message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun verifyCode(code: String) {

        val credential = PhoneAuthProvider.getCredential(codeGenerated, code)
        signInWithPhoneAuthCredential(credential)

    }



    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        val auth = FirebaseAuth.getInstance()

        auth.signInWithCredential(credential)
                .addOnCompleteListener(requireActivity()) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d("signIn", "signInWithCredential:success")

                        val user = task.result?.user
                        Toast.makeText(requireContext(), "Verification successful", Toast.LENGTH_SHORT).show()

                        val action = PhoneVerificationFragmentDirections.actionPhoneVerificationFragmentToHomeFragment()
                        findNavController().navigate(action)
                    } else {
                        // Sign in failed, display a message and update the UI
                        Log.w("signIn", "signInWithCredential:failure", task.exception)
                        if (task.exception is FirebaseAuthInvalidCredentialsException) {
                            // The verification code entered was invalid
                            Toast.makeText(requireContext(), "Verification Invalid", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
    }

}