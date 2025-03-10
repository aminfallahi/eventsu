package com.aminfallahi.eventsu.general.order

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.aminfallahi.eventsu.general.R
import com.aminfallahi.eventsu.general.attendees.Attendee
import com.aminfallahi.eventsu.general.event.Event
import kotlin.collections.ArrayList

class OrderDetailsRecyclerAdapter : RecyclerView.Adapter<OrderDetailsViewHolder>() {

    private val attendees = ArrayList<Attendee>()
    private var event: Event? = null
    private var orderIdentifier: String? = null
    private var eventDetailsListener: EventDetailsListener? = null

    fun addAll(attendeeList: List<Attendee>) {
        if (attendees.isNotEmpty())
            this.attendees.clear()
        this.attendees.addAll(attendeeList)
    }

    fun setEvent(event: Event?) {
        this.event = event
    }

    fun setListener(listener: EventDetailsListener) {
        eventDetailsListener = listener
    }

    fun setOrderIdentifier(orderId: String?) {
        orderIdentifier = orderId
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderDetailsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_card_order_details, parent, false)
        return OrderDetailsViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrderDetailsViewHolder, position: Int) {
        val order = attendees[position]
        holder.bind(order, event, orderIdentifier, eventDetailsListener)
    }

    override fun getItemCount(): Int {
        return attendees.size
    }

    interface EventDetailsListener {
        fun onClick(eventID: Long)
    }
}
