package com.ewdev.parkitnow.view.fragments

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ewdev.parkitnow.R
import com.ewdev.parkitnow.data.AddedCarsRecycleViewAdapter
import com.ewdev.parkitnow.data.ParkedCar
import com.ewdev.parkitnow.viewModel.ParkCarViewModel
import kotlinx.android.synthetic.main.fragment_park_car.*
import android.text.format.DateFormat
import java.util.*

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
            leave_time_tv.text = DateFormat.format("yyyy-MM-dd\n HH:mm:ss", leaveTime)
        })

        // TODO: add date
        leave_time_tv.setOnClickListener {

            val currentSetCal: Calendar = viewModel.leaveTime.value ?: Calendar.getInstance()
            Log.i("cal", currentSetCal.toString())
            val datePickerDialog = DatePickerDialog(
                    requireContext(),
                    object: DatePickerDialog.OnDateSetListener {
                        override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
                            // *do stuff*

                            val timePickerDialog = TimePickerDialog(
                                    requireContext(),
                                    object : TimePickerDialog.OnTimeSetListener {
                                        override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
                                            val calendar = GregorianCalendar(
                                                    year,
                                                    month,
                                                    dayOfMonth,
                                                    hourOfDay,
                                                    minute
                                            )
                                            viewModel.onTimeSet(calendar)
                                        }
                                    },
                                    currentSetCal.get(Calendar.HOUR),
                                    currentSetCal.get(Calendar.MINUTE),
                                    true
                            )
//            timePickerDialog.updateTime(viewModel.leaveTime.value!!.hour, viewModel.leaveTime.value!!.minute)
                            timePickerDialog.show()
                        }
                    },
                    currentSetCal.get(Calendar.YEAR),
                    currentSetCal.get(Calendar.MONTH),
                    currentSetCal.get(Calendar.DAY_OF_MONTH)
            )
            datePickerDialog.show()


        }

        fab_add.setOnClickListener {
            findNavController().navigate(R.id.action_parkCarFragment_to_addCarFragment)
        }

        initList()

        // TODO("fix navigation, currently when popping backstack weird things happen, for example pressing the back button makes the past arg remain and so we end up with two same licensePlates")
        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<ParkedCar>("car")?.observe(viewLifecycleOwner, Observer {
            result -> viewModel.addCar(result)
        })

        fab_next.setOnClickListener {
            viewModel.parkCar()
            findNavController().navigate(R.id.action_parkCarFragment_to_homeParkedFragment)
        }
    }

    private fun initList() {
        rv_cars_added.apply {
            layoutManager = LinearLayoutManager(requireActivity())
        }

        viewModel.blockedCars.observe(viewLifecycleOwner, { cars ->
            var listAdapter = AddedCarsRecycleViewAdapter(
                    cars,
                    { car -> viewModel.removeFromList(car) }
            )
            rv_cars_added.apply {
                layoutManager = layoutManager
                adapter = listAdapter
            }
        })

        viewModel.retrieveCars()
    }

}