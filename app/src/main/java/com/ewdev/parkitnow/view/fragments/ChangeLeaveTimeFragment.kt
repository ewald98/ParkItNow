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
import android.text.format.DateFormat
import com.ewdev.parkitnow.viewModel.ChangeLeaveTimeViewModel
import kotlinx.android.synthetic.main.fragment_change_leave_time.*
import java.util.*

class ChangeLeaveTimeFragment: Fragment() {

    lateinit var viewModel: ChangeLeaveTimeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this).get(ChangeLeaveTimeViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_change_leave_time, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.leaveTime.observe(viewLifecycleOwner, Observer { leaveTime ->
            new_leave_time.text = DateFormat.format("yyyy-MM-dd\n HH:mm:ss", leaveTime)
        })

        new_leave_time.setOnClickListener {

            val currentSetCal: Calendar = viewModel.leaveTime.value ?: Calendar.getInstance()
            val datePickerDialog = DatePickerDialog(
                    requireContext(),
                    object: DatePickerDialog.OnDateSetListener {
                        override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {

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

        fab_next_btn.setOnClickListener {
            viewModel.changeLeaveTime()
            viewModel.changesCommited.observe(viewLifecycleOwner, {
                findNavController().navigate(R.id.action_changeLeaveTimeFragment_to_homeParkedFragment)
            })
        }
    }

}