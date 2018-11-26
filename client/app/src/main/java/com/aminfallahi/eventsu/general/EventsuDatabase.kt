package com.aminfallahi.eventsu.general

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import com.aminfallahi.eventsu.general.attendees.Attendee
import com.aminfallahi.eventsu.general.attendees.AttendeeDao
import com.aminfallahi.eventsu.general.attendees.AttendeeIdConverter
import com.aminfallahi.eventsu.general.attendees.ListAttendeeIdConverter
import com.aminfallahi.eventsu.general.attendees.forms.CustomForm
import com.aminfallahi.eventsu.general.auth.User
import com.aminfallahi.eventsu.general.auth.UserDao
import com.aminfallahi.eventsu.general.event.Event
import com.aminfallahi.eventsu.general.event.EventDao
import com.aminfallahi.eventsu.general.event.EventIdConverter
import com.aminfallahi.eventsu.general.event.topic.EventTopic
import com.aminfallahi.eventsu.general.event.topic.EventTopicIdConverter
import com.aminfallahi.eventsu.general.event.topic.EventTopicsDao
import com.aminfallahi.eventsu.general.order.Order
import com.aminfallahi.eventsu.general.order.OrderDao
import com.aminfallahi.eventsu.general.social.SocialLink
import com.aminfallahi.eventsu.general.social.SocialLinksDao
import com.aminfallahi.eventsu.general.ticket.Ticket
import com.aminfallahi.eventsu.general.ticket.TicketIdConverter
import com.aminfallahi.eventsu.general.ticket.TicketDao

@Database(entities = [Event::class, User::class, SocialLink::class, Ticket::class, Attendee::class, EventTopic::class, Order::class, CustomForm::class], version = 1)
@TypeConverters(EventIdConverter::class, EventTopicIdConverter::class, TicketIdConverter::class, AttendeeIdConverter::class, ListAttendeeIdConverter::class)
abstract class OpenEventDatabase : RoomDatabase() {

    abstract fun eventDao(): EventDao

    abstract fun userDao(): UserDao

    abstract fun ticketDao(): TicketDao

    abstract fun socialLinksDao(): SocialLinksDao

    abstract fun attendeeDao(): AttendeeDao

    abstract fun eventTopicsDao(): EventTopicsDao

    abstract fun orderDao(): OrderDao
}
