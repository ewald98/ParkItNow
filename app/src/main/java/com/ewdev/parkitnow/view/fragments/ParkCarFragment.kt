package com.ewdev.parkitnow.view.fragments

import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TimePicker
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.ewdev.parkitnow.R
import com.ewdev.parkitnow.data.CarsRecycleViewAdapter
import com.ewdev.parkitnow.data.ParkedCar
import com.ewdev.parkitnow.viewModel.ParkCarViewModel
import kotlinx.android.synthetic.main.fragment_park_car.*
import java.util.Objects.isNull

class ParkCarFragment: Fragment() {

    lateinit var viewModel: ParkCarViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this).get(ParkCarViewModel::class.java)
    }

    // TODO 2: set blocked Cars (hmm.. only directly blocked, or as many as you want?)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_park_car, container, false)
    }

//    val args: ParkCarFragmentArgs by navArgs()
//
//    override fun onResume() {
//        super.onResume()
//
//        if (args.car != null) {
//            Log.i("added_car", args.car!!.licensePlate)
//
//            viewModel.addCar(args.car!!)
//        }
//    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.leaveTime.observe(viewLifecycleOwner, Observer { leaveTime ->
            leave_time_tv.text = leaveTime.toString()
        })

        leave_time_tv.setOnClickListener {
            val timePickerDialog = TimePickerDialog(
                    context,
                    object : TimePickerDialog.OnTimeSetListener {
                        override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
                            viewModel.onTimeSet(hourOfDay, minute)
                        }
                    },
                    viewModel.leaveTime.value?.hour ?: 12,
                    viewModel.leaveTime.value?.minute ?: 0,
                    false
            )
//            timePickerDialog.updateTime(viewModel.leaveTime.value!!.hour, viewModel.leaveTime.value!!.minute)
            timePickerDialog.show()

        }

        fab_add.setOnClickListener {
            findNavController().navigate(R.id.action_parkCarFragment_to_addCarFragment)
        }

        initList()

        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<ParkedCar>("car")?.observe(viewLifecycleOwner, Observer {
            result -> viewModel.addCar(result)
        })

        fab_next.setOnClickListener {


            findNavController().navigate(R.id.action_parkCarFragment_to_homeParkedFragment)
        }
    }

    private fun initList() {
        rv_blocked_cars.apply {
            layoutManager = LinearLayoutManager(requireActivity())
        }

        viewModel.blockedCars.observe(viewLifecycleOwner, { cars ->
            var listAdapter = CarsRecycleViewAdapter(
                    cars,
                    { car -> viewModel.removeFromList(car) }
            )
            rv_blocked_cars.apply {
                layoutManager = layoutManager
                adapter = listAdapter
            }
        })

        viewModel.retrieveCars()
    }

}