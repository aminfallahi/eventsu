package com.aminfallahi.eventsu.general.event

import android.arch.persistence.room.TypeConverter

class EventIdConverter {

    @TypeConverter
    fun fromEventId(eventId: EventId?): Long? {
        return eventId?.id
    }

    @TypeConverter
    fun toEventId(id: Long?): EventId? {
        return id?.let {
            EventId(it)
        }
    }
}
