package com.ewdev.parkitnow.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.ewdev.parkitnow.R
import com.ewdev.parkitnow.viewModel.SettingsViewModel
import kotlinx.android.synthetic.main.fragment_home_unparked.*
import kotlinx.android.synthetic.main.fragment_settings.*
import kotlinx.android.synthetic.main.fragment_settings.drawer_layout
import kotlinx.android.synthetic.main.fragment_settings.topAppBar

class SettingsFragment : Fragment() {

    lateinit var viewModel: SettingsViewModel
    // TODO: selecting a car should only be possible if you aren't parked.

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(SettingsViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        nav_view_settings.setNavigationItemSelectedListener() { menuItem ->
            if (menuItem.itemId == R.id.nav_home) {
                findNavController().navigate(R.id.action_settingsFragment_to_homeUnparkedFragment)
            }
            return@setNavigationItemSelectedListener false
        }

        val toggle = ActionBarDrawerToggle(requireActivity(), drawer_layout, topAppBar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        viewModel.selectedCar.observe(viewLifecycleOwner, { licensePlate ->
            if (!licensePlate.isEmpty())
                tv_car_license_plate.text = licensePlate
        })

        viewModel.changesCommited.observe(viewLifecycleOwner, { success ->
            if (!success)
                Toast.makeText(requireContext(), "Error!", Toast.LENGTH_SHORT).show()
            else
                Toast.makeText(requireContext(), "Success!", Toast.LENGTH_SHORT).show()
        })

        set_license_plate_btn.setOnClickListener {
            viewModel.setCarLicensePlate(set_license_plate_edtx.text.toString())
        }
    }

}