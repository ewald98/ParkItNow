package com.ewdev.parkitnow.data

import android.opengl.Visibility
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ewdev.parkitnow.R
import kotlinx.android.synthetic.main.item_blocked_car.view.*

class BlockedCarsRecycleViewAdapter(
    private val parkedCars: List<RelativeParkedCar>
): RecyclerView.Adapter<BlockedCarsRecycleViewAdapter.CarViewHolder>() {

    inner class CarViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        fun bind(car: RelativeParkedCar) {
            itemView.tv_license_plate.text = car.licensePlate
            itemView.tv_leave_time.text = car.relativeDepartureTime
            if (car.relativeDepartureTime[1] == 'm' || car.relativeDepartureTime[2] == 'm')
                itemView.leaving_soon_warning_icon.visibility = View.VISIBLE
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_blocked_car, parent, false)
        return CarViewHolder(view)
    }

    override fun onBindViewHolder(holder: CarViewHolder, position: Int) {
        holder.bind(parkedCars[position])
    }

    override fun getItemCount(): Int {
        return parkedCars.size
    }

}
