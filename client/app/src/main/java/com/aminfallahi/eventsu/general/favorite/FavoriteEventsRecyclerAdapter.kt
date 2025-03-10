package com.aminfallahi.eventsu.general.favorite

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.aminfallahi.eventsu.general.R
import com.aminfallahi.eventsu.general.event.Event
import com.aminfallahi.eventsu.general.event.EventViewHolder
import com.aminfallahi.eventsu.general.event.FavoriteFabListener
import com.aminfallahi.eventsu.general.event.RecyclerViewClickListener
import java.util.*

class FavoriteEventsRecyclerAdapter : RecyclerView.Adapter<EventViewHolder>() {
    private val events = ArrayList<Event>()
    private var clickListener: RecyclerViewClickListener? = null
    private var favoriteFabListener: FavoriteFabListener? = null

    fun setListener(listener: RecyclerViewClickListener) {
        clickListener = listener
    }

    fun setFavorite(listener: FavoriteFabListener) {
        favoriteFabListener = listener
    }

    fun addAll(eventList: List<Event>) {
        if (events.isNotEmpty())
            this.events.clear()
        this.events.addAll(eventList)
    }

    fun getPos(id: Long): Int {
        return events.map { it.id }.indexOf(id)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_card_favorite_event, parent, false)
        return EventViewHolder(view)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = events[position]
        holder.bind(event, clickListener, favoriteFabListener, FAVORITE_EVENT_DATE_FORMAT)
    }

    override fun getItemCount(): Int {
        return events.size
    }
}
