package com.pexpress.pexpresscustomer.view.main.order.dialog.maps.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.pexpress.pexpresscustomer.R
import com.pexpress.pexpresscustomer.databinding.ItemsPlacePredictionBinding

class PlacePredictionAdapter :
    RecyclerView.Adapter<PlacePredictionAdapter.PlacePredictionViewHolder>() {

    private var onPlaceItemClickCallBack: OnPlaceItemClickCallBack? = null
    private val predictions: MutableList<AutocompletePrediction> = ArrayList()

    fun setPredictions(predictions: List<AutocompletePrediction>?) {
        this.predictions.clear()
        this.predictions.addAll(predictions!!)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlacePredictionViewHolder {
        val binding =
            ItemsPlacePredictionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PlacePredictionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlacePredictionViewHolder, position: Int) {
        val place = predictions[position]
        holder.setPrediction(place)
        holder.itemView.setOnClickListener {
            onPlaceItemClickCallBack?.onPlaceClicked(place)
        }
    }

    override fun getItemCount() = predictions.size

    class PlacePredictionViewHolder(private val binding: ItemsPlacePredictionBinding) :
        ViewHolder(binding.root) {
        fun setPrediction(prediction: AutocompletePrediction) {
            with(binding) {
                titlePlace.text = prediction.getPrimaryText(null)
                address.text = prediction.getSecondaryText(null)
            }
        }

        fun setBackground(state: Boolean) {
            binding.bgItemsPlace.setBackgroundColor(
                if (state) {
                    ContextCompat.getColor(
                        itemView.context,
                        R.color.grayLight
                    )
                } else {
                    ContextCompat.getColor(
                        itemView.context,
                        R.color.backgroundLight
                    )
                },
            )
        }
    }

    fun setOnPlaceItemClickCallBack(onPlaceItemClickCallBack: OnPlaceItemClickCallBack) {
        this.onPlaceItemClickCallBack = onPlaceItemClickCallBack
    }

    interface OnPlaceItemClickCallBack {
        fun onPlaceClicked(place: AutocompletePrediction)
    }

    interface OnPlaceItemClickChangeBackground
}