package com.ewdev.parkitnow.view.fragments

import android.app.TimePickerDialog
import android.os.Bundle
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TimePicker
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.ewdev.parkitnow.R
import com.ewdev.parkitnow.viewModel.ParkCarViewModel
import kotlinx.android.synthetic.main.fragment_park_car.*
import java.util.*

class ParkCarFragment: Fragment() {

    lateinit var viewModel: ParkCarViewModel

    var leaveHour: Int = 0
    var leaveMinute: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this).get(ParkCarViewModel::class.java)

        leaveHour = viewModel._hour
        leaveHour = viewModel._minute
    }
    // TODO 1: set departure time
    // TODO 2: set blocked Cars (hmm.. only directly blocked, or as many as you want?)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_park_car, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val calendar: Calendar = Calendar.getInstance()
        calendar.set(0, 0, 0, leaveHour, leaveMinute)
        leave_time_tv.text = DateFormat.format("hh:mm aa", calendar)

        leave_time_tv.setOnClickListener {

            val timePickerDialog: TimePickerDialog = TimePickerDialog(
                    context,
                    object : TimePickerDialog.OnTimeSetListener {
                        override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
                            viewModel.setSelectedTime(hourOfDay, minute)
                            leaveHour = hourOfDay
                            leaveMinute = minute

                            // TODO: make sure (now > date)
                            val calendar: Calendar = Calendar.getInstance()
                            calendar.set(0, 0, 0, leaveHour, leaveMinute)
                            leave_time_tv.text = DateFormat.format("hh:mm aa", calendar)
                        }
                    },
                    leaveHour,
                    leaveMinute,
                    false
            )

            timePickerDialog.updateTime(leaveHour, leaveMinute)
            timePickerDialog.show()



        }

        fab_add.setOnClickListener {

            findNavController().navigate(R.id.action_parkCarFragment_to_addCarFragment)
        }

    }

}