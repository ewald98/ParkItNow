package com.ewdev.parkitnow.view.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.ewdev.parkitnow.R
import com.ewdev.parkitnow.view.fragments.PhoneAuthenticationFragmentDirections
import kotlinx.android.synthetic.main.fragment_phone_authentication.*

class PhoneAuthenticationFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_phone_authentication, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        next_button.setOnClickListener {
            // TODO: add more conditition for phone_number to be correct
            val phoneNumber = "+4" + phone_number.text.toString().trim()
            Log.i("phone_number", phoneNumber)

            val action = PhoneAuthenticationFragmentDirections.actionPhoneAuthenticationFragmentToPhoneVerificationFragment(phoneNumber)
            findNavController().navigate(action)
        }


    }

}