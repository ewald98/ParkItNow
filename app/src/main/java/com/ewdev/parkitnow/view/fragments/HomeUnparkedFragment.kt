package com.ewdev.parkitnow.view.fragments

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.ewdev.parkitnow.R
import com.ewdev.parkitnow.viewModel.HomeUnparkedViewModel
import kotlinx.android.synthetic.main.fragment_home_unparked.*
import kotlinx.android.synthetic.main.fragment_home_unparked.topAppBar


class HomeUnparkedFragment: Fragment() {

//    private val mToolbar: MaterialToolbar
    lateinit var viewModel: HomeUnparkedViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        viewModel = ViewModelProvider(this).get(HomeUnparkedViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home_unparked, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.refresh -> {
                    viewModel.refreshFragment()
                    true
                }
                R.id.log_out -> {
                    viewModel.logOut()
                    findNavController().navigate(R.id.action_homeUnparkedFragment_to_phoneAuthenticationFragment)
                    true
                }
                else -> false
            }
        }

        viewModel.refreshFragment.observe(viewLifecycleOwner, {
            findNavController().navigate(R.id.action_homeUnparkedFragment_self)
        })

        nav_view.setNavigationItemSelectedListener() { menuItem ->
            if (menuItem.itemId == R.id.nav_settings) {
                findNavController().navigate(R.id.action_homeUnparkedFragment_to_settingsFragment)
            }
            return@setNavigationItemSelectedListener false
        }

        val toggle = ActionBarDrawerToggle(requireActivity(), drawer_layout, topAppBar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        park_car_button.setOnClickListener {
            if (welcome_greeting_unparked.text.equals("Hello, please set your car")) {
                Toast.makeText(requireContext(), "Please set your car license plate before you park!!", Toast.LENGTH_SHORT).show()
            } else {
                findNavController().navigate(R.id.action_homeUnparkedFragment_to_parkCarFragment)
            }
        }

        viewModel.userCarLicensePlate.observe(viewLifecycleOwner, { carLicensePlate ->
            welcome_greeting_unparked.text = "Hello, " + carLicensePlate
        })

        viewModel.timesInQueue.observe(viewLifecycleOwner, { timesInQueue ->
            tv_times_in_queue.text = timesInQueue.toString()
            tv_points.text = timesInQueue.toString() + " ParkItNows"
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
    }

}

