package com.udayrana.roamto_uday

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.udayrana.roamto_uday.databinding.PlaceItemBinding

class PlaceAdapter(
    private val placeList: MutableList<Place>,
    private val placeClickListener: PlaceClickListener
) : RecyclerView.Adapter<PlaceAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: PlaceItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = PlaceItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return placeList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val place = placeList[position]

        holder.binding.textViewPlaceTitle.text = place.title
        holder.binding.textViewPlaceDate.text = place.date

        holder.binding.linearLayoutItemText.setOnClickListener {
            placeClickListener.displayPlaceDetails(
                place
            )
        }
        holder.binding.imageViewDelete.setOnClickListener { placeClickListener.deletePlace(place) }
    }
}