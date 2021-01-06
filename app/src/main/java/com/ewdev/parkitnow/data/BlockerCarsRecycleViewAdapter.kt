package com.ewdev.parkitnow.data

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ewdev.parkitnow.R
import kotlinx.android.synthetic.main.item_blocker_car.view.*

class BlockerCarsRecycleViewAdapter(
    private val parkedCars: List<RelativeParkedCar>,
    private val callUser: (String) -> (Unit)
): RecyclerView.Adapter<BlockerCarsRecycleViewAdapter.CarViewHolder>() {

    inner class CarViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        fun bind(car: RelativeParkedCar) {
            itemView.tv_license_plate.text = car.licensePlate
            itemView.tv_leave_time.text = car.relativeDepartureTime
            itemView.call.setOnClickListener {
                callUser(car.licensePlate)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_blocker_car, parent, false)
        return CarViewHolder(view)
    }

    override fun onBindViewHolder(holder: CarViewHolder, position: Int) {
        holder.bind(parkedCars[position])
    }

    override fun getItemCount(): Int {
        return parkedCars.size
    }

}
