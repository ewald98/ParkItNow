package com.ewdev.parkitnow.view.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.ewdev.parkitnow.R
import com.ewdev.parkitnow.viewModel.PhoneVerificationViewModel
import com.ewdev.parkitnow.viewModel.PhoneVerificationViewModelFactory
import com.google.firebase.auth.PhoneAuthOptions
import kotlinx.android.synthetic.main.fragment_phone_verification.*
import java.util.concurrent.TimeUnit

class PhoneVerificationFragment : Fragment() {

    private lateinit var phoneVerificationViewModel: PhoneVerificationViewModel
//    private lateinit var binding: FragmentPhoneVerificationBinding

    // TODO: shouldn't be able to get to this fragment after getting to home

    val args: PhoneVerificationFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i("phone_number_received", args.phoneNumber)
        phoneVerificationViewModel = ViewModelProvider(requireActivity(), PhoneVerificationViewModelFactory(
            requireActivity().application, args.phoneNumber))
            .get(PhoneVerificationViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

//  TODO: Try this data binding thing
//        binding = DataBindingUtil.inflate(
//            inflater,
//            R.layout.fragment_phone_verification,
//            container,
//            false
//        )
//
//        binding.phoneVerificationViewModel = phoneVerificationViewModel

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_phone_verification, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        phoneVerificationViewModel.smsCode.observe(viewLifecycleOwner, { code ->
            pin_view.setText(code)

        })

        phoneVerificationViewModel.phoneVerified.observe(viewLifecycleOwner, { signedIn ->
            if (signedIn) {
                phoneVerificationViewModel.isParked.observe(viewLifecycleOwner, { isParked ->
                    if (isParked) {
                        val action = PhoneVerificationFragmentDirections.actionPhoneVerificationFragmentToHomeParkedFragment()
                        findNavController().navigate(action)
                        Log.i("nav_action", "completed: action_splashFragment_to_homeParkedFragment")
                    } else {
                        val action = PhoneVerificationFragmentDirections.actionPhoneVerificationFragmentToHomeUnparkedFragment()
                        findNavController().navigate(action)
                        Log.i("nav_action", "completed: action_splashFragment_to_homeUnparkedFragment")
                    }
                })
            } else {

            }
        })

        phoneVerificationViewModel.callbackLiveData.observe(viewLifecycleOwner, { callback ->

            phoneVerificationViewModel.sendVerificationCode(
                PhoneAuthOptions.newBuilder()
                    .setPhoneNumber(args.phoneNumber)
                    .setTimeout(60L, TimeUnit.SECONDS)
                    .setActivity(requireActivity())
                    .setCallbacks(callback)
                    .build())
        })

        next_button.setOnClickListener {
            val code = pin_view.text.toString()
            if (!code.isEmpty()) {
                phoneVerificationViewModel.verifyCode(code)
            }
        }

        phoneVerificationViewModel.requestAuthOptions()
    }

}