package com.aminfallahi.eventsu.general.order

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.aminfallahi.eventsu.general.R
import com.aminfallahi.eventsu.general.event.Event

class OrdersRecyclerAdapter : RecyclerView.Adapter<OrdersViewHolder>() {

    private val eventAndOrderIdentifier = ArrayList<Pair<Event, String>>()
    private var clickListener: OrderClickListener? = null
    var attendeesNumber = ArrayList<Int>()

    fun setListener(listener: OrderClickListener) {
        clickListener = listener
    }

    fun addAllPairs(list: List<Pair<Event, String>>) {
        if (eventAndOrderIdentifier.isNotEmpty())
            this.eventAndOrderIdentifier.clear()
        eventAndOrderIdentifier.addAll(list)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrdersViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_card_order, parent, false)
        return OrdersViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrdersViewHolder, position: Int) {
        holder.bind(eventAndOrderIdentifier[position].first, clickListener, eventAndOrderIdentifier[position].second, attendeesNumber[position])
    }

    override fun getItemCount(): Int {
        return eventAndOrderIdentifier.size
    }

    fun setAttendeeNumber(number: ArrayList<Int>) {
        attendeesNumber = number
    }

    interface OrderClickListener {
        fun onClick(eventID: Long, orderIdentifier: String)
    }
}
