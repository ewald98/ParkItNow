package com.ewdev.parkitnow.data

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ewdev.parkitnow.R
import kotlinx.android.synthetic.main.item_car.view.*

class CarsRecycleViewAdapter(
        private val relativeParkedCars: List<RelativeParkedCar>,
        private val onDeleteClick: (RelativeParkedCar) -> (Unit)
): RecyclerView.Adapter<CarsRecycleViewAdapter.CarViewHolder>() {

    inner class CarViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        init {
            itemView.ibt_delete.setOnClickListener {
                onDeleteClick(relativeParkedCars[adapterPosition])
            }

        }

        fun bind(car: RelativeParkedCar) {
            itemView.tv_license_plate.text = car.licensePlate
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_car, parent, false)
        return CarViewHolder(view)
    }

    override fun onBindViewHolder(holder: CarViewHolder, position: Int) {
        holder.bind(relativeParkedCars[position])
    }

    override fun getItemCount(): Int {
        return relativeParkedCars.size
    }

}