package com.ewdev.parkitnow.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.ewdev.parkitnow.R
import com.ewdev.parkitnow.viewModel.HomeFragmentViewModel
import kotlinx.android.synthetic.main.fragment_home_parked.*

class HomeParkedFragment : Fragment() {

    lateinit var viewModel: HomeFragmentViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this).get(HomeFragmentViewModel::class.java)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home_parked, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.blockedCars.observe(viewLifecycleOwner, Observer { parkedCars ->
            var s = ""
            parkedCars.forEach{
                s += it.licensePlate + " " + it.relativeDepartureTime + "\n"
            }
            blocked_tv.text = s
        })

        viewModel.blockerCars.observe(viewLifecycleOwner, Observer { parkedCars ->
            var s = ""
            parkedCars.forEach{
                s += it.licensePlate + " " + it.relativeDepartureTime + "\n"
            }
            blocker_tv.text = s
        })


    }
}