package com.ewdev.parkitnow.view.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.ewdev.parkitnow.R
import kotlinx.android.synthetic.main.fragment_home_unparked.*


class HomeUnparkedFragment: Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home_unparked, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.i("am ajuns", "viewc")
        park_car_button.setOnClickListener {
            Log.i("am ajuns", "clikc")
            findNavController().navigate(R.id.action_homeUnparkedFragment_to_parkCarFragment)
        }
    }

}

