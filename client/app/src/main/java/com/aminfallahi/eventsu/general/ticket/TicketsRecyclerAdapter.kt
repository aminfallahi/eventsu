package com.aminfallahi.eventsu.general.ticket

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.aminfallahi.eventsu.general.R
import java.util.*

class TicketsRecyclerAdapter : RecyclerView.Adapter<TicketViewHolder>() {

    private val tickets = ArrayList<Ticket>()
    private var eventCurrency: String? = null
    private var selectedListener: TicketSelectedListener? = null

    fun addAll(ticketList: List<Ticket>) {
        if (tickets.isNotEmpty())
            this.tickets.clear()
        this.tickets.addAll(ticketList)
    }

    fun setSelectListener(listener: TicketSelectedListener) {
        selectedListener = listener
    }

    fun setCurrency(currencyCode: String?) {
        eventCurrency = currencyCode
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TicketViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_ticket, parent, false)
        return TicketViewHolder(view)
    }

    override fun onBindViewHolder(holder: TicketViewHolder, position: Int) {
        val event = tickets[position]
        holder.bind(event, selectedListener, eventCurrency)
    }

    override fun getItemCount(): Int {
        return tickets.size
    }
}

interface TicketSelectedListener {
    fun onSelected(ticketId: Int, quantity: Int)
}
