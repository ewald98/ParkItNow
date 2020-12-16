package com.ewdev.parkitnow.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.ewdev.parkitnow.R
import com.ewdev.parkitnow.data.ParkedCar
import com.ewdev.parkitnow.viewModel.AddCarViewModel
import kotlinx.android.synthetic.main.fragment_add_car.*
import java.util.*

class AddCarFragment: Fragment() {

    lateinit var viewModel: AddCarViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(AddCarViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_add_car, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fab_finish.setOnClickListener {

            val car = ParkedCar("whatevz", add_car_edtx.text.toString(), Date())

            viewModel.requestCarValidity(car)
            viewModel.validCar.observe(viewLifecycleOwner, androidx.lifecycle.Observer { valid ->
                if (valid) {
                    findNavController().previousBackStackEntry?.savedStateHandle?.set("car", car)
                    findNavController().popBackStack()
                } else {
                    Toast.makeText(
                            requireActivity(),
                            "Invalid car" + car.licensePlate,
                            Toast.LENGTH_SHORT
                    ).show()
                }
            })



//            onDestroy()
        }

    }

}