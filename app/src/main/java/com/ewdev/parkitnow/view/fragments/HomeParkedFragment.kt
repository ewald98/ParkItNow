package com.ewdev.parkitnow.view.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ewdev.parkitnow.R
import com.ewdev.parkitnow.data.BlockedCarsRecycleViewAdapter
import com.ewdev.parkitnow.data.BlockerCarsRecycleViewAdapter
import com.ewdev.parkitnow.data.CarState
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

        initStats()

        // decide which buttons to show.
        viewModel.carState.observe(viewLifecycleOwner, { carState ->
            when (carState) {
                CarState.LEAVER -> {
                    leave_now_button.visibility = View.GONE
                    change_leave_time_button.visibility = View.GONE
                    configureILeftButton()
                    i_left_button.visibility = View.VISIBLE
                }
                CarState.LEAVE_ANNOUNCER -> {
                    leave_now_button.visibility = View.GONE
                    change_leave_time_button.visibility = View.GONE
                    configureLeaverLeftButton()
                    viewModel.carLeavingLicensePlate.observe(viewLifecycleOwner, { leaverLicensePlate ->
                        leaver_left_button.text = leaverLicensePlate + "left"
                        tv_departureTime_prefix.text = "The following car is in the process of leaving:"
                        tv_departureTime.text = leaverLicensePlate
                        leaver_left_button.visibility = View.VISIBLE
                    })
                }
                else -> { }
            }
        })

        initLists()

        leave_now_button.setOnClickListener {
            viewModel.leaveNow()
        }

        viewModel.refreshFragment.observe(viewLifecycleOwner, {
            findNavController().navigate(R.id.action_homeParkedFragment_self)
        })

    }

    private fun configureLeaverLeftButton() {
        leaver_left_button.setOnClickListener {
            viewModel.cleanQueue()
            viewModel.refreshFragment()
        }
    }

    private fun configureILeftButton() {
        i_left_button.setOnClickListener {
            viewModel.cleanQueue()
            viewModel.refreshFragment()
        }
    }

    private fun initStats() {
        viewModel.leaveTime.observe(viewLifecycleOwner, { time ->
            tv_departureTime.text = time
        })
    }

    private fun initLists() {
        rv_blocked_cars_list.apply {
            layoutManager = LinearLayoutManager(requireActivity())
        }
        rv_blocker_cars_list.apply {
            layoutManager = LinearLayoutManager(requireActivity())
        }

        viewModel.blockedCars.observe(viewLifecycleOwner, { cars ->
            if (cars.isEmpty())
                tv_blocked_no_one.visibility = View.VISIBLE
            else {
                tv_blocked_no_one.visibility = View.GONE
                var listAdapter = BlockedCarsRecycleViewAdapter(
                    cars
                )
                rv_blocked_cars_list.apply {
                    layoutManager = layoutManager
                    adapter = listAdapter
                }
            }
        })
        viewModel.blockerCars.observe(viewLifecycleOwner, Observer { cars ->
            if (cars.isEmpty())
                tv_blocker_no_one.visibility = View.VISIBLE
            else {
                tv_blocker_no_one.visibility = View.GONE
                var listAdapter = BlockerCarsRecycleViewAdapter(
                    cars,
                    { licensePlate -> callUser(licensePlate) }
                )
                rv_blocker_cars_list.apply {
                    layoutManager = layoutManager
                    adapter = listAdapter
                }
            }
        })

    }

    private fun callUser(licensePlate: String) {
        viewModel.getUserPhoneNumber(licensePlate)
        viewModel.phoneNo.observe(viewLifecycleOwner, { phoneNo ->
            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNo))
            startActivity(intent)
        })
    }

}