package com.example.weatherapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.models.geocoding.LocationResponseItem
import com.example.weathertst.R

import kotlinx.android.synthetic.main.saved_item.view.*

class SavedAdapter(var list: List<LocationResponseItem>): RecyclerView.Adapter<SavedAdapter.SavedViewHolder>() {

    inner class SavedViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
   }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavedViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.saved_item, parent, false)
        return SavedViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: SavedViewHolder, position: Int) {
        val location = list[position]

        holder.itemView.apply {
            location_in_rv_saved.text = location.name.toString()

            delete_in_rv_saved.setOnClickListener {
                onItemClickListenerDelete?.let {
                    it(location)
                }
            }

            setOnClickListener {
                onItemClickListener?.let {
                    it(location)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    private var onItemClickListener: ((LocationResponseItem) -> Unit)? = null

    fun setOnItemClickListener(listener: (LocationResponseItem) -> Unit) {
        onItemClickListener = listener
    }

    private var onItemClickListenerDelete: ((LocationResponseItem) -> Unit)? = null

    fun setOnItemClickListenerDelete(listener: (LocationResponseItem) -> Unit) {
        onItemClickListenerDelete = listener
    }

}