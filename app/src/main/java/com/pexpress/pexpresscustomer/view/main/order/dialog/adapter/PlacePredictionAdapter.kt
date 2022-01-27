package com.pexpress.pexpresscustomer.view.main.order.dialog.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.pexpress.pexpresscustomer.databinding.ItemsPlacePredictionBinding
import java.util.*

/**
 * A [RecyclerView.Adapter] for a [com.google.android.libraries.places.api.model.AutocompletePrediction].
 */
class PlacePredictionAdapter :
    RecyclerView.Adapter<PlacePredictionAdapter.PlacePredictionViewHolder>() {
    private val predictions: MutableList<AutocompletePrediction> = ArrayList()

    var onPlaceClickListener: ((AutocompletePrediction) -> Unit)? = null

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
            onPlaceClickListener?.invoke(place)
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
    }

    interface OnPlaceClickListener {
        fun onPlaceClicked(place: AutocompletePrediction)
    }
}