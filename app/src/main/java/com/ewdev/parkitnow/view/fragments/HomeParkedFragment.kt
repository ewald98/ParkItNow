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
import androidx.recyclerview.widget.LinearLayoutManager
import com.ewdev.parkitnow.R
import com.ewdev.parkitnow.data.BlockedCarsRecycleViewAdapter
import com.ewdev.parkitnow.data.BlockerCarsRecycleViewAdapter
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

        viewModel.leaveTime.observe(viewLifecycleOwner, { time ->
            tv_departureTime.text = time
        })

        initLists()

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

        leave_now_button.setOnClickListener {
            viewModel.leaveNow()
        }

    }

    private fun initLists() {
        rv_blocked_cars_list.apply {
            layoutManager = LinearLayoutManager(requireActivity())
        }
        rv_blocker_cars_list.apply {
            layoutManager = LinearLayoutManager(requireActivity())
        }

    }

    private fun callUser(licensePlate: String) {
        viewModel.getUserPhoneNumber(licensePlate)
        viewModel.phoneNo.observe(viewLifecycleOwner, { phoneNo ->
            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNo))
            startActivity(intent)
        })
    }

}